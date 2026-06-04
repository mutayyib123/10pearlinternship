import React, {useState} from 'react'
import { useNavigate } from 'react-router-dom'
import api, { setToken } from '../services/api'

export default function Login(){
  const [email,setEmail]=useState('')
  const [password,setPassword]=useState('')
  const [msg,setMsg]=useState('')
  const navigate = useNavigate()

  const submit = async (e)=>{
    e.preventDefault()
    try{
      const res = await api.post('/auth/login', { email, password })
      const token = res?.data?.token
      if (token) setToken(token)
      setMsg(res.data.message || 'Login success')
      // navigate to dashboard after short delay
      setTimeout(()=> navigate('/'), 400)
    }catch(err){
      setMsg(err?.response?.data?.message || 'Login failed')
    }
  }


  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={submit}>
        <div>
          <label>Email</label>
          <input value={email} onChange={e=>setEmail(e.target.value)} placeholder="you@example.com" />
        </div>
        <div>
          <label>Password</label>
          <input type="password" value={password} onChange={e=>setPassword(e.target.value)} placeholder="••••••" />
        </div>
        <div className="row">
          <button type="submit" disabled={!email || !password}>Login</button>
          <button type="button" className="secondary" onClick={()=>navigate('/register')}>Sign up</button>
        </div>
      </form>
      {msg && <div className="message">{msg}</div>}
    </div>
  )
}
