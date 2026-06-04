import React, {useState} from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../services/api'

export default function Register(){
  const [name,setName]=useState('')
  const [email,setEmail]=useState('')
  const [password,setPassword]=useState('')
  const [msg,setMsg]=useState('')
  const submit = async (e)=>{
    e.preventDefault()
    try{
      const res = await api.post('/auth/register', { name, email, password })
      setMsg('Registered successfully')
      setTimeout(()=> navigate('/login'), 600)
    }catch(err){
      setMsg(err?.response?.data?.message || 'Registration failed')
    }
  }

  const navigate = useNavigate()

  return (
    <div>
      <h2>Register</h2>
      <form onSubmit={submit}>
        <div>
          <label>Name</label>
          <input value={name} onChange={e=>setName(e.target.value)} placeholder="Full name" />
        </div>
        <div>
          <label>Email</label>
          <input value={email} onChange={e=>setEmail(e.target.value)} placeholder="you@example.com" />
        </div>
        <div>
          <label>Password</label>
          <input type="password" value={password} onChange={e=>setPassword(e.target.value)} placeholder="At least 6 characters" />
        </div>
        <div className="row">
          <button type="submit" disabled={!name || !email || !password}>Register</button>
          <button type="button" className="secondary" onClick={()=>navigate('/login')}>Back to login</button>
        </div>
      </form>
      {msg && <div className="message">{msg}</div>}
    </div>
  )
}
