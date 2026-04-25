import { useState } from 'react'
import { createOrder } from '../api/toyApi'
import './Cart.css'

function Cart({ cart, setCart, onClose, user }) {
  const [selected, setSelected] = useState([])
  const [showOrderForm, setShowOrderForm] = useState(false)
  const [customerName, setCustomerName] = useState(user?.name || '')
  const [customerEmail, setCustomerEmail] = useState(user?.email || '')

  // Группируем корзину: { id, name, brand, price, quantity }
  const grouped = cart.reduce((acc, item) => {
    const existing = acc.find(i => i.id === item.id)
    if (existing) {
      existing.quantity += 1
    } else {
      acc.push({ ...item, quantity: 1 })
    }
    return acc
  }, [])

  const toggleSelect = (id) => {
    setSelected(prev =>
      prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]
    )
  }

  const removeItem = (id) => {
    setCart(cart.filter(item => item.id !== id))
    setSelected(selected.filter(i => i !== id))
  }

  const selectedItems = grouped.filter(item => selected.includes(item.id))
  const totalItems = selectedItems.reduce((sum, item) => sum + item.quantity, 0)
  const totalPrice = selectedItems.reduce((sum, item) => sum + item.price * item.quantity, 0)

  const handleOrder = () => {
    if (selectedItems.length === 0) {
      alert('Выбери товары для заказа')
      return
    }
    setShowOrderForm(true)
  }

  const submitOrder = async () => {
    if (!customerName || !customerEmail) {
      alert('Заполни имя и email')
      return
    }
    try {
      const items = selectedItems.map(item => ({
        toyId: item.id,
        quantity: item.quantity,
      }))

      await createOrder({
        customerId: user.id,
        items: items,
      })
      alert('Заказ оформлен!')

      // Удаляем выбранные товары из корзины
      const selectedIds = selected
      const remaining = cart.filter(item => !selectedIds.includes(item.id))
      setCart(remaining)
      setSelected([])
      setShowOrderForm(false)
      onClose()
    } catch (err) {
      console.error('Ошибка заказа:', err)
      alert('Ошибка оформления заказа')
    }
  }

  return (
    <div className="cart-overlay">
      <div className="cart-panel">
        <div className="cart-header">
          <h2>🛒 Корзина</h2>
          <button className="close-btn" onClick={onClose}>✕</button>
        </div>

        {grouped.length === 0 ? (
          <p className="cart-empty">Корзина пуста</p>
        ) : (
          <>
            <div className="cart-items">
              {grouped.map((item) => (
                <div key={item.id} className="cart-item">
                  <input
                    type="checkbox"
                    className="cart-checkbox"
                    checked={selected.includes(item.id)}
                    onChange={() => toggleSelect(item.id)}
                  />
                  <div className="cart-item-info">
                    <p className="cart-item-name">{item.name}</p>
                    <p className="cart-item-brand">{item.brand}</p>
                  </div>
                  <p className="cart-item-qty">{item.quantity} шт.</p>
                  <p className="cart-item-price">{item.price * item.quantity} Br</p>
                  <button className="remove-btn" onClick={() => removeItem(item.id)}>✕</button>
                </div>
              ))}
            </div>

            {selectedItems.length > 0 && (
              <div className="cart-summary">
                <p>Выбрано: {selectedItems.length} поз., {totalItems} шт.</p>
                <p className="cart-total">Сумма: {totalPrice} Br</p>
              </div>
            )}

            <button className="order-btn" onClick={handleOrder}>
              Оформить заказ
            </button>
          </>
        )}

        {showOrderForm && (
          <div className="order-form">
            <h3>Данные для заказа</h3>
            <input
              placeholder="Имя"
              value={customerName}
              onChange={(e) => setCustomerName(e.target.value)}
              required
            />
            <input
              type="email"
              placeholder="Email"
              value={customerEmail}
              onChange={(e) => setCustomerEmail(e.target.value)}
              required
            />
            <div className="order-form-actions">
              <button onClick={submitOrder}>Подтвердить</button>
              <button className="btn-back" onClick={() => setShowOrderForm(false)}>Назад</button>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default Cart