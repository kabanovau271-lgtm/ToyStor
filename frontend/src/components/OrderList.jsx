import { useState, useEffect } from 'react'
import { getOrders, deleteOrder } from '../api/toyApi'
import './OrderList.css'

function OrderList({ onClose, customerId }) {
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadOrders()
  }, [])

  const loadOrders = async () => {
    try {
      const res = await getOrders()
      setOrders(res.data)
    } catch (err) {
      console.error('Ошибка загрузки заказов:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Удалить заказ?')) return
    try {
      await deleteOrder(id)
      loadOrders()
    } catch (err) {
      alert('Ошибка удаления')
    }
  }

  const getTotal = (items) => {
    if (!items) return 0
    return items.reduce((sum, item) => sum + (item.priceAtPurchase || 0) * (item.quantity || 0), 0)
  }

  const filteredOrders = customerId
    ? orders.filter(o => o.customer?.id === customerId)
    : orders

  return (
    <div className="modal-overlay">
      <div className="modal orders-modal">
        <div className="cart-header">
          <h2>📦 Заказы</h2>
          <button className="close-btn" onClick={onClose}>✕</button>
        </div>

        {loading ? (
          <p className="loading">Загрузка...</p>
        ) : filteredOrders.length === 0 ? (
          <p className="cart-empty">Заказов пока нет</p>
        ) : (
          <div className="orders-list">
            {filteredOrders.map((order, index) => (
              <div key={order.id} className="order-card">
                <div className="order-header">
                  <span className="order-id">Заказ №{index + 1}</span>
                  <span className="order-date">
                    {new Date(order.createdAt).toLocaleDateString('ru-RU', {
                      day: 'numeric', month: 'long', year: 'numeric',
                      hour: '2-digit', minute: '2-digit'
                    })}
                  </span>
                  {!customerId && (
                    <button className="remove-btn" onClick={() => handleDelete(order.id)}>🗑️</button>
                  )}
                </div>
                <div className="order-items">
                  {order.items?.map((item, i) => (
                    <div key={i} className="order-item">
                      <span className="order-item-name">
                        {item.toy?.name || `Игрушка #${item.toy?.id}`}
                      </span>
                      <span className="order-item-qty">{item.quantity} шт. × {item.priceAtPurchase || item.toy?.price || 0} Br</span>
                    </div>
                  ))}
                </div>
                <div className="order-footer">
                  <span>Клиент: {order.customer?.name || `#${order.customer?.id}`}</span>
                  <span className="order-total">Итого: {getTotal(order.items)} Br</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default OrderList