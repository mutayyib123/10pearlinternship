import React, {useState} from 'react'
import api from '../services/api'

export default function Profile(){
  const [info,setInfo]=useState({name:'User',email:'user@example.com'})
  const [showChange,setShowChange]=useState(false)
  const [current,setCurrent]=useState('')
  const [newPass,setNewPass]=useState('')

  const changePassword = async ()=>{
    try{
      await api.post('/auth/change-password', { email: info.email, currentPassword: current, newPassword: newPass })
      alert('Password changed')
      setShowChange(false)
    }catch(err){ alert('Failed') }
  }

  const logout = ()=>{
    // clear token and notify
    localStorage.removeItem('cm_token')
    alert('Logged out (client-side)')
  }

  return (
    <div>
      <h2>Profile</h2>
      <div>Name: {info.name}</div>
      <div>Email: {info.email}</div>
      <div style={{marginTop:10}}>
        <button onClick={()=>setShowChange(true)}>Change Password</button>
        <button onClick={logout}>Logout</button>
      </div>

      {showChange && (
        <div style={{border:'1px solid #ccc',padding:10,marginTop:10}}>
          <h3>Change Password</h3>
          <div>
            <label>Current</label>
            <input value={current} onChange={e=>setCurrent(e.target.value)} />
          </div>
          <div>
            <label>New</label>
            <input value={newPass} onChange={e=>setNewPass(e.target.value)} />
          </div>
          <div style={{marginTop:8}}>
            <button onClick={changePassword}>Change</button>
            <button onClick={()=>setShowChange(false)}>Cancel</button>
          </div>
        </div>
      )}
    </div>
  )
}
