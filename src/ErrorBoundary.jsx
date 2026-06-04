import React from 'react'

export default class ErrorBoundary extends React.Component {
  constructor(props){ super(props); this.state = { error: null, info: null } }
  static getDerivedStateFromError(error){ return { error } }
  componentDidCatch(error, info){ this.setState({ info }); console.error('ErrorBoundary caught', error, info) }
  render(){
    if(this.state.error){
      return (
        <div style={{padding:20}}>
          <h2 style={{color:'crimson'}}>An error occurred</h2>
          <div style={{whiteSpace:'pre-wrap',background:'#fff5f5',padding:12,borderRadius:6}}>
            {String(this.state.error)}
            {this.state.info && '\n' + (this.state.info.componentStack || '')}
          </div>
        </div>
      )
    }
    return this.props.children
  }
}
