import { myAxios } from '@/request'

/**
 * 用户注册
 * @param params
 * @returns
 */
export const userRegister = async (params: any) => {
  return myAxios.request({
    url: '/api/user/register',
    method: 'POST',
    data: params,
  })
}

/**
 * 用户登录
 * @param params
 * @returns
 */
export const userLogin = async (params: any) => {
  return myAxios.request({
    url: '/api/user/login',
    method: 'POST',
    data: params,
    // 重要：允许携带cookie，这样后端设置的httpOnly cookie会自动保存
    withCredentials: true,
  })
}

/**
 * 获取当前用户信息
 * @returns
 */
export const getCurrentUser = async () => {
  return myAxios.request({
    url: '/api/user/current',
    method: 'GET',
    // 重要：允许携带cookie，这样会自动发送httpOnly token
    withCredentials: true,
  })
}

/**
 * 获取用户列表
 * @returns
 */
export const getUserList = async (username: any) => {
  return myAxios.request({
    url: '/api/user/list',
    method: 'GET',
    params: {
      username,
    },
  })
}

/**
 * 用户登出
 * @returns
 */
export const userLogout = async () => {
  return myAxios.request({
    url: '/api/user/logout',
    method: 'POST',
    // 重要：允许携带cookie，用于清除服务端session
    withCredentials: true,
  })
}

/**
 * 更新用户信息
 * @param params
 * @returns
 */
export const updateUser = async (params: any) => {
  return myAxios.request({
    url: '/api/user/update',
    method: 'POST',
    data: params,
  })
}

/**
 * 删除用户
 * @param params
 * @returns
 */
export const deleteUser = async (id: string) => {
  return myAxios.request({
    url: '/api/user/delete',
    method: 'POST',
    data: id,
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
