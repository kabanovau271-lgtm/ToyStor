import { useState, useEffect } from 'react'
import axios from 'axios'
import './OrderList.css'

function CustomerList({ onClose }) {
  const [customers, setCustomers] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadCustomers()
  }, [])

  const loadCustomers = async () => {
    try {
      const res = await axios.get('http://localhost:8080/customers')
      setCustomers(res.data)
    } catch (err) {
      console.error('Ошибка загрузки пользователей:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Удалить пользователя?')) return
    try {
      await axios.delete(`http://localhost:8080/customers/${id}`)
      loadCustomers()
    } catch (err) {
      alert('Ошибка удаления')
    }
  }

  return (
    <div className="modal-overlay">
      <div className="modal orders-modal">
        <div className="cart-header">
          <h2>👥 Пользователи</h2>
          <button className="close-btn" onClick={onClose}>✕</button>
        </div>

        {loading ? (
          <p className="loading">Загрузка...</p>
        ) : customers.length === 0 ? (
          <p className="cart-empty">Пользователей пока нет</p>
        ) : (
          <div className="orders-list">
            {customers.map(c => (
              <div key={c.id} className="order-card">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div>
                    <p style={{ fontWeight: 'bold', color: '#5D4037' }}>{c.name}</p>
                    <p style={{ color: '#999', fontSize: '14px' }}>{c.email}</p>
                    <p style={{ color: '#C9A87C', fontSize: '13px', marginTop: '4px' }}>
                      Роль: {c.role === 'ADMIN' ? '👑 Админ' : '👤 Покупатель'}
                    </p>
                  </div>
                  <button
                    onClick={() => handleDelete(c.id)}
                    style={{
                      background: 'none',
                      border: 'none',
                      cursor: 'pointer',
                      fontSize: '18px',
                    }}
                  >
                    🗑️
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default CustomerList