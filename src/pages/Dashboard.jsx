import React, {useEffect, useState} from 'react'
import api from '../services/api'
import Modal from '../components/Modal'

export default function Dashboard(){
  const [contacts,setContacts]=useState([])
  const [q,setQ]=useState('')
  const [editing,setEditing]=useState(null)
  const [showAdd,setShowAdd]=useState(false)
  const [form,setForm]=useState({firstName:'',lastName:'',title:'',userId:1})
  const [loading,setLoading]=useState(false)
  const [message,setMessage]=useState('')

  useEffect(()=>{ fetchContacts() },[])

  const fetchContacts = async ()=>{
    setLoading(true)
    try{
      const res = await api.get('/contacts')
      setContacts(res.data.content || res.data || [])
    }catch(err){ console.error(err); setMessage('Failed to load contacts') }
    setLoading(false)
  }

  const search = async ()=>{
    setLoading(true)
    try{
      // mock backend expects 'query' param; backend may also accept 'q'
      const res = await api.get('/contacts/search', { params: { query: q } })
      setContacts(res.data.content || res.data || [])
    }catch(err){ console.error(err); setMessage('Search failed') }
    setLoading(false)
  }

  const save = async ()=>{
    // basic validation
    if(!form.firstName || !form.lastName){ setMessage('First and last name required'); return }
    setLoading(true)
    try{
      if(editing){
        await api.put(`/contacts/${editing}`, form)
        setMessage('Contact updated')
      }else{
        await api.post('/contacts', form)
        setMessage('Contact created')
      }
      setShowAdd(false)
      setEditing(null)
      setForm({firstName:'',lastName:'',title:'',userId:1})
      fetchContacts()
    }catch(err){ console.error(err); setMessage('Save failed') }
    setLoading(false)
  }

  const remove = async (id)=>{
    if(!confirm('Delete?')) return
    setLoading(true)
    try{
      await api.delete(`/contacts/${id}`)
      setMessage('Contact deleted')
      fetchContacts()
    }catch(err){ console.error(err); setMessage('Delete failed') }
    setLoading(false)
  }

  const startEdit = (c)=>{
    setEditing(c.id)
    setForm({firstName:c.firstName,lastName:c.lastName,title:c.title,userId:c.userId})
    setShowAdd(true)
  }

  return (
    <div>
      <h2>Dashboard</h2>
      {message && <div className="message">{message}</div>}
      <div className="controls" style={{marginTop:10}}>
        <input placeholder="Search by name" value={q} onChange={e=>setQ(e.target.value)} />
        <div className="row">
          <button onClick={search} disabled={loading}>Search</button>
          <button onClick={()=>{setShowAdd(true); setEditing(null); setForm({firstName:'',lastName:'',title:'',userId:1})}} disabled={loading}>Add Contact</button>
        </div>
      </div>

      <table className="table" style={{marginTop:12}}>
        <thead><tr><th>ID</th><th>First</th><th>Last</th><th>Title</th><th>Actions</th></tr></thead>
        <tbody>
          {contacts.length===0 && <tr><td colSpan="5" className="small center">{loading? 'Loading...' : 'No contacts'}</td></tr>}
          {contacts.map(c=> (
            <tr key={c.id}>
              <td>{c.id}</td>
              <td>{c.firstName}</td>
              <td>{c.lastName}</td>
              <td>{c.title}</td>
              <td>
                <button onClick={()=>startEdit(c)} disabled={loading}>Edit</button>
                <button onClick={()=>remove(c.id)} disabled={loading}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {showAdd && (
        <Modal onClose={()=>setShowAdd(false)}>
          <h3>{editing? 'Edit' : 'Add'} Contact</h3>
          <div className="form-row">
            <div>
              <label>First</label>
              <input value={form.firstName} onChange={e=>setForm({...form,firstName:e.target.value})} />
            </div>
            <div>
              <label>Last</label>
              <input value={form.lastName} onChange={e=>setForm({...form,lastName:e.target.value})} />
            </div>
          </div>
          <div style={{marginTop:8}}>
            <label>Title</label>
            <input value={form.title} onChange={e=>setForm({...form,title:e.target.value})} />
          </div>
          <div className="inline-actions">
            <button onClick={save} disabled={loading}>{editing? 'Save' : 'Create'}</button>
            <button className="secondary" onClick={()=>setShowAdd(false)} disabled={loading}>Cancel</button>
          </div>
        </Modal>
      )}
    </div>
  )
}
