import { useState, useEffect } from 'react'
import { getToys, searchToys, filterToys, getCategories, createToy, updateToy, deleteToy } from './api/toyApi'
import Login from './pages/Login'
import './App.css'
import ToyForm from './components/ToyForm'
import Cart from './components/Cart'
import OrderList from './components/OrderList'
import CustomerList from './components/CustomerList'

const CATEGORY_COLORS = {
  'Мягкие игрушки': '#FFB5A7',
  'Конструкторы': '#BDE0FE',
  'Куклы': '#FFC8DD',
  'Машинки': '#A2D2FF',
  'Развивающие': '#CAFFBF',
  'Настольные игры': '#FDFFB6',
}


const PAGE_SIZE = 6

function App() {
  const [user, setUser] = useState(null)
  const [toys, setToys] = useState([])
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(true)
  const [cart, setCart] = useState([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)

  const [showForm, setShowForm] = useState(false)
  const [editingToy, setEditingToy] = useState(null)
  const [showCart, setShowCart] = useState(false)
  const [showOrders, setShowOrders] = useState(false)
  const [showMyOrders, setShowMyOrders] = useState(false)
  const [showCustomers, setShowCustomers] = useState(false)

  const [showFilter, setShowFilter] = useState(false)
  const [categories, setCategories] = useState([])
  const [selectedCategory, setSelectedCategory] = useState('')
  const [minPrice, setMinPrice] = useState('')
  const [maxPrice, setMaxPrice] = useState('')

  useEffect(() => {
    loadToys()
    loadCategories()
  }, [])

  const loadCategories = async () => {
    try {
      const res = await getCategories()
      setCategories(res.data)
    } catch (err) {
      console.error('Ошибка загрузки категорий:', err)
    }
  }

  const loadToys = async (pageNum = 0) => {
    try {
      setLoading(true)
      const res = await getToys(pageNum, PAGE_SIZE)
      setToys(res.data.content)
      setTotalPages(res.data.totalPages)
      setPage(res.data.number)
    } catch (err) {
      console.error('Ошибка загрузки:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleSearch = async () => {
    if (!search.trim()) { loadToys(); return }
    try {
      setLoading(true)
      const res = await searchToys(search)
      setToys(Array.isArray(res.data) ? res.data : res.data.content || [])
      setTotalPages(1)
      setPage(0)
    } catch (err) {
      console.error('Ошибка поиска:', err)
    } finally {
      setLoading(false)
    }
  }

  const applyFilter = async () => {
    try {
      setLoading(true)
      const params = {
        category: selectedCategory || '',
        minPrice: minPrice || '0',
        maxPrice: maxPrice || '999999',
        page: 0,
        size: PAGE_SIZE,
      }
      const res = await filterToys(params)
      setToys(res.data.content)
      setTotalPages(res.data.totalPages)
      setPage(0)
      setShowFilter(false)
    } catch (err) {
      console.error('Ошибка фильтрации:', err)
    } finally {
      setLoading(false)
    }
  }

  const resetFilter = () => {
    setSelectedCategory('')
    setMinPrice('')
    setMaxPrice('')
    loadToys()
    setShowFilter(false)
  }

  const addToCart = (toy) => { setCart([...cart, toy]) }

const getToyImage = (toy) => {
  return `/images/${toy.id}.jpg`
}

  const getInitial = (name) => {
    const words = name.split(' ')
    if (words.length >= 2) return (words[0][0] + words[1][0]).toUpperCase()
    return name.slice(0, 2).toUpperCase()
  }

  const handleAdd = () => {
    setEditingToy(null)
    setShowForm(true)
  }

  const handleEdit = (toy) => {
    setEditingToy(toy)
    setShowForm(true)
  }

  const handleSave = async (data) => {
    try {
      if (editingToy) {
        await updateToy(editingToy.id, data)
      } else {
        await createToy(data)
      }
      setShowForm(false)
      setEditingToy(null)
      loadToys()
    } catch (err) {
      console.error('Ошибка сохранения:', err)
      alert('Ошибка сохранения')
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Удалить игрушку?')) return
    try {
      await deleteToy(id)
      loadToys()
    } catch (err) {
      console.error('Ошибка удаления:', err)
      alert('Ошибка удаления')
    }
  }

  const isAdmin = user?.role === 'ADMIN'

  if (!user) {
    return <Login onLogin={(userData) => setUser(userData)} />
  }

  return (
    <div className="app">


     <header className="header">
       <h1>🐣 Капитошка</h1>
       <div className="search-bar">
         <input
           type="text"
           placeholder="Поиск игрушек..."
           value={search}
           onChange={(e) => setSearch(e.target.value)}
           onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
         />
         <button onClick={handleSearch}>🔍</button>
         <button className="filter-btn" onClick={() => setShowFilter(!showFilter)}>☰</button>
       </div>
       <div className="header-actions">
         {!isAdmin && (
           <button className="header-btn my-orders-btn" onClick={() => setShowMyOrders(true)}>
             📦 Мои заказы
           </button>
         )}
         <button className="header-btn logout-btn-header" onClick={() => setUser(null)}>
           Выйти
         </button>
       </div>
     </header>



      {showFilter && (
        <div className="filter-panel">
          <h3>Фильтр</h3>
          <div className="filter-body">
            <div className="filter-section">
              <h4>Категория</h4>
              <div className="filter-radios">
                <label className="filter-radio">
                  <input type="radio" name="category" checked={selectedCategory === ''} onChange={() => setSelectedCategory('')} />
                  Все
                </label>
                {categories.map(cat => (
                  <label key={cat.id} className="filter-radio">
                    <input type="radio" name="category" checked={selectedCategory === cat.name} onChange={() => setSelectedCategory(cat.name)} />
                    {cat.name}
                  </label>
                ))}
              </div>
            </div>
            <div className="filter-section">
              <h4 className="price-title">Цена</h4>
              <div className="price-range">
                <input type="number" placeholder="От" value={minPrice} onChange={(e) => setMinPrice(e.target.value)} />
                <span>—</span>
                <input type="number" placeholder="До" value={maxPrice} onChange={(e) => setMaxPrice(e.target.value)} />
              </div>
            </div>
          </div>
          <div className="filter-actions">
            <button onClick={applyFilter}>Применить</button>
            <button className="btn-reset" onClick={resetFilter}>Сбросить</button>
          </div>
        </div>
      )}

      {isAdmin && (
        <div style={{ display: 'flex', justifyContent: 'center', gap: '10px', marginBottom: '30px' }}>
          <button className="add-toy-btn" onClick={handleAdd}>➕ Добавить игрушку</button>
          <button className="add-toy-btn" onClick={() => setShowOrders(true)} style={{ background: '#C9A87C', color: 'white' }}>
            📦 Заказы
          </button>
          <button className="add-toy-btn" onClick={() => setShowCustomers(true)} style={{ background: '#D4A574', color: 'white' }}>
            👥 Пользователи
          </button>
        </div>
      )}

      {loading ? (
        <p className="loading">Загрузка...</p>
      ) : (
        <>
          <div className="toys-grid">
            {toys.map((toy) => {
              const imgSrc = getToyImage(toy)
              return (
                <div key={toy.id} className="toy-card">
                  <div className="toy-image">
                    <img
                      src={imgSrc}
                      alt={toy.name}
                      className="toy-img"
                      onError={(e) => {
                        e.target.style.display = 'none'
                        e.target.nextSibling.style.display = 'flex'
                      }}
                    />
                    <div
                      className="toy-image-placeholder"
                      style={{ display: 'none', background: CATEGORY_COLORS[toy.categories?.[0]] || '#E8D5B7' }}
                    >
                      <span className="toy-initial">{getInitial(toy.name)}</span>
                    </div>
                  </div>
                  <div className="toy-info">
                    <h3>{toy.name}</h3>
                    <p className="toy-brand">🏷️ {toy.brand}</p>
                    <div className="toy-categories">
                      {toy.categories?.map((cat) => (
                        <span key={cat} className="category-tag">{cat}</span>
                      ))}
                    </div>
                    <p className="toy-price">{toy.price} Br</p>
                    <p className="toy-stock">
                      {toy.quantity > 0 ? `✅ В наличии: ${toy.quantity} шт.` : '❌ Нет в наличии'}
                    </p>
                    {isAdmin ? (
                      <div className="admin-buttons">
                        <button className="btn-edit" onClick={() => handleEdit(toy)}>✏️</button>
                        <button className="btn-delete" onClick={() => handleDelete(toy.id)}>🗑️</button>
                      </div>
                    ) : (
                      <button disabled={toy.quantity === 0} className="btn-cart" onClick={() => addToCart(toy)}>
                        🛒 В корзину
                      </button>
                    )}
                  </div>
                </div>
              )
            })}
          </div>

          {totalPages > 1 && (
            <div className="pagination">
              <button disabled={page === 0} onClick={() => loadToys(page - 1)}>‹</button>
              {Array.from({ length: totalPages }, (_, i) => (
                <button key={i} className={page === i ? 'active' : ''} onClick={() => loadToys(i)}>
                  {i + 1}
                </button>
              ))}
              <button disabled={page === totalPages - 1} onClick={() => loadToys(page + 1)}>›</button>
            </div>
          )}
        </>
      )}

      {showForm && (
        <ToyForm
          toy={editingToy}
          onSave={handleSave}
          onCancel={() => { setShowForm(false); setEditingToy(null) }}
        />
      )}

      {showOrders && <OrderList onClose={() => setShowOrders(false)} />}
      {showMyOrders && <OrderList onClose={() => setShowMyOrders(false)} customerId={user.id} />}
      {showCustomers && <CustomerList onClose={() => setShowCustomers(false)} />}

      {!isAdmin && cart.length > 0 && (
        <div className="cart-fixed" onClick={() => setShowCart(true)}>
          🛒 <span className="cart-count">{cart.length}</span>
        </div>
      )}

      {showCart && (
        <Cart
          cart={cart}
          setCart={setCart}
          user={user}
          onClose={() => setShowCart(false)}
        />
      )}
    </div>
  )
}

export default App