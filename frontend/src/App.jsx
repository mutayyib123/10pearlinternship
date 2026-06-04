import React from 'react'
import { Outlet, Link } from 'react-router-dom'

export default function App(){
  return (
    <div className="app">
      <header>
        <h1>Contact Management</h1>
        <nav>
          <Link to="/">Dashboard</Link> | <Link to="/profile">Profile</Link> | <Link to="/login">Login</Link>
        </nav>
      </header>
      <main>
        <Outlet />
      </main>
    </div>
  )
}
