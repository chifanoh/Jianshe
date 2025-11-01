/**
 * IndexedDB 工具类
 */

// 数据库配置
const DB_NAME = 'miku_app_db'
const DB_VERSION = 1 // 升级版本号以触发数据库结构更新
const STORE_NAME = 'user_data'

// 数据库实例
let db: IDBDatabase | null = null

/**
 * 生成ISO格式时间戳字符串 "YYYY-MM-DD HH:mm:ss_SSSZ"
 */
const generateTimestampKey = (): string => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const hours = String(now.getHours()).padStart(2, '0')
  const minutes = String(now.getMinutes()).padStart(2, '0')
  const seconds = String(now.getSeconds()).padStart(2, '0')
  const milliseconds = String(now.getMilliseconds()).padStart(3, '0')
  const timezone = String(-now.getTimezoneOffset() / 60).padStart(2, '0')

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}_${milliseconds}Z${timezone}`
}

/**
 * 初始化 IndexedDB
 */
export const initDB = (): Promise<IDBDatabase> => {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open(DB_NAME, DB_VERSION)

    request.onerror = () => {
      reject(new Error('Failed to open IndexedDB'))
    }

    request.onsuccess = () => {
      db = request.result
      resolve(db)
    }

    request.onupgradeneeded = (event) => {
      const database = (event.target as IDBOpenDBRequest).result

      // 如果存在旧的存储，先删除它
      if (database.objectStoreNames.contains(STORE_NAME)) {
        database.deleteObjectStore(STORE_NAME)
      }

      // 创建新的用户数据存储
      const store = database.createObjectStore(STORE_NAME, { keyPath: 'key' })
      store.createIndex('key', 'key', { unique: true })
    }
  })
}

/**
 * 保存用户数据到新格式
 * key格式："${timestamp}"（ISO格式时间戳字符串）
 * value格式：{ key: "${timestamp}", data: {用户数据对象} }
 */
export const saveUserData = async (key: string, data: any): Promise<void> => {
  if (!db) {
    await initDB()
  }

  return new Promise((resolve, reject) => {
    const transaction = db!.transaction([STORE_NAME], 'readwrite')
    const store = transaction.objectStore(STORE_NAME)

    const request = store.put({
      key: key, // ISO格式时间戳字符串
      data: data, // 用户数据对象
    })

    request.onsuccess = () => {
      resolve()
    }

    request.onerror = () => {
      reject(new Error('Failed to save user data'))
    }
  })
}

/**
 * 获取用户数据
 */
export const getUserData = async (key: string): Promise<any> => {
  if (!db) {
    await initDB()
  }

  return new Promise((resolve, reject) => {
    const transaction = db!.transaction([STORE_NAME], 'readonly')
    const store = transaction.objectStore(STORE_NAME)

    const request = store.get(key)

    request.onsuccess = () => {
      const result = request.result
      if (result) {
        resolve(result.data) // 返回用户数据对象
      } else {
        resolve(null)
      }
    }

    request.onerror = () => {
      reject(new Error('Failed to get user data'))
    }
  })
}

/**
 * 删除用户数据
 */
export const removeUserData = async (key: string): Promise<void> => {
  if (!db) {
    await initDB()
  }

  return new Promise((resolve, reject) => {
    const transaction = db!.transaction([STORE_NAME], 'readwrite')
    const store = transaction.objectStore(STORE_NAME)

    const request = store.delete(key)

    request.onsuccess = () => {
      resolve()
    }

    request.onerror = () => {
      reject(new Error('Failed to remove user data'))
    }
  })
}

/**
 * 清除所有用户数据
 */
export const clearAllUserData = async (): Promise<void> => {
  if (!db) {
    await initDB()
  }

  return new Promise((resolve, reject) => {
    const transaction = db!.transaction([STORE_NAME], 'readwrite')
    const store = transaction.objectStore(STORE_NAME)

    const request = store.clear()

    request.onsuccess = () => {
      resolve()
    }

    request.onerror = () => {
      reject(new Error('Failed to clear user data'))
    }
  })
}

/**
 * 检查数据是否过期（基于时间戳字符串比较）
 */
export const isDataExpired = async (
  key: string,
  maxAge: number = 7 * 24 * 60 * 60 * 1000, // 默认7天
): Promise<boolean> => {
  if (!db) {
    await initDB()
  }

  return new Promise((resolve, reject) => {
    const transaction = db!.transaction([STORE_NAME], 'readonly')
    const store = transaction.objectStore(STORE_NAME)

    const request = store.get(key)

    request.onsuccess = () => {
      const result = request.result
      if (result) {
        // 从key中解析时间戳
        const keyTimestamp = new Date(key.split('_')[0]).getTime()
        const currentTime = new Date().getTime()
        const age = currentTime - keyTimestamp
        resolve(age > maxAge)
      } else {
        resolve(true) // 数据不存在，视为过期
      }
    }

    request.onerror = () => {
      reject(new Error('Failed to check data expiration'))
    }
  })
}

/**
 * 获取最新的用户数据（基于时间戳排序）
 */
export const getLatestUserData = async (): Promise<any> => {
  if (!db) {
    await initDB()
  }

  return new Promise((resolve, reject) => {
    const transaction = db!.transaction([STORE_NAME], 'readonly')
    const store = transaction.objectStore(STORE_NAME)

    // 获取所有数据
    const request = store.getAll()

    request.onsuccess = () => {
      const results = request.result
      if (results && results.length > 0) {
        // 按时间戳字符串排序，最新的在前
        results.sort((a, b) => {
          const timeA = new Date(a.key.split('_')[0]).getTime()
          const timeB = new Date(b.key.split('_')[0]).getTime()
          return timeB - timeA // 降序排列
        })
        resolve(results[0].data) // 返回最新的用户数据
      } else {
        resolve(null)
      }
    }

    request.onerror = () => {
      reject(new Error('Failed to get latest user data'))
    }
  })
}

/**
 * 生成新的时间戳key并保存用户数据
 * 这是一个便捷方法，自动生成时间戳key并保存数据
 */
export const saveUserDataWithTimestamp = async (data: any): Promise<string> => {
  const timestampKey = generateTimestampKey()
  await saveUserData(timestampKey, data)
  return timestampKey
}

export default {
  initDB,
  saveUserData,
  getUserData,
  removeUserData,
  clearAllUserData,
  isDataExpired,
  getLatestUserData,
  saveUserDataWithTimestamp,
  generateTimestampKey,
}
