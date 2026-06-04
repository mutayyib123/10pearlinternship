import React, {useEffect, useState} from 'react'
import api from '../services/api'

export default function Dashboard(){
  const [contacts,setContacts]=useState([])
  const [q,setQ]=useState('')
  const [editing,setEditing]=useState(null)
  const [showAdd,setShowAdd]=useState(false)
  const [form,setForm]=useState({firstName:'',lastName:'',title:'',userId:1})

  useEffect(()=>{ fetchContacts() },[])

  const fetchContacts = async ()=>{
    try{
      const res = await api.get('/contacts')
      setContacts(res.data.content || res.data || [])
    }catch(err){ console.error(err) }
  }

  const search = async ()=>{
    try{
      const res = await api.get('/contacts/search', { params: { q } })
      setContacts(res.data.content || res.data || [])
    }catch(err){ console.error(err) }
  }

  const save = async ()=>{
    try{
      if(editing){
        await api.put(`/contacts/${editing}`, form)
      }else{
        await api.post('/contacts', form)
      }
      setShowAdd(false)
      setEditing(null)
      setForm({firstName:'',lastName:'',title:'',userId:1})
      fetchContacts()
    }catch(err){ console.error(err) }
  }

  const remove = async (id)=>{
    if(!confirm('Delete?')) return
    try{
      await api.delete(`/contacts/${id}`)
      fetchContacts()
    }catch(err){ console.error(err) }
  }

  const startEdit = (c)=>{
    setEditing(c.id)
    setForm({firstName:c.firstName,lastName:c.lastName,title:c.title,userId:c.userId})
    setShowAdd(true)
  }

  return (
    <div>
      <h2>Dashboard</h2>
      <div>
        <input placeholder="Search" value={q} onChange={e=>setQ(e.target.value)} />
        <button onClick={search}>Search</button>
        <button onClick={()=>{setShowAdd(true); setEditing(null); setForm({firstName:'',lastName:'',title:'',userId:1})}}>Add</button>
      </div>
      <table border="1" cellPadding="6" style={{marginTop:10}}>
        <thead><tr><th>ID</th><th>First</th><th>Last</th><th>Title</th><th>Actions</th></tr></thead>
        <tbody>
          {contacts.map(c=> (
            <tr key={c.id}>
              <td>{c.id}</td>
              <td>{c.firstName}</td>
              <td>{c.lastName}</td>
              <td>{c.title}</td>
              <td>
                <button onClick={()=>startEdit(c)}>Edit</button>
                <button onClick={()=>remove(c.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {showAdd && (
        <div style={{border:'1px solid #ccc',padding:10,marginTop:10}}>
          <h3>{editing? 'Edit' : 'Add'} Contact</h3>
          <div>
            <label>First</label>
            <input value={form.firstName} onChange={e=>setForm({...form,firstName:e.target.value})} />
          </div>
          <div>
            <label>Last</label>
            <input value={form.lastName} onChange={e=>setForm({...form,lastName:e.target.value})} />
          </div>
          <div>
            <label>Title</label>
            <input value={form.title} onChange={e=>setForm({...form,title:e.target.value})} />
          </div>
          <div style={{marginTop:8}}>
            <button onClick={save}>{editing? 'Save' : 'Create'}</button>
            <button onClick={()=>setShowAdd(false)}>Cancel</button>
          </div>
        </div>
      )}
    </div>
  )
}
