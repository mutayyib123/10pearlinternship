import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' }
})

// Attach token from localStorage if present
api.interceptors.request.use(cfg => {
  const token = localStorage.getItem('cm_token')
  if (token) cfg.headers = { ...cfg.headers, Authorization: `Bearer ${token}` }
  return cfg
})

const setToken = (token) => {
  if (token) localStorage.setItem('cm_token', token)
  else localStorage.removeItem('cm_token')
}

export default api
export { setToken }
