import { useState, useEffect } from 'react'
import { getBrands, getCategories, createBrand, createCategory, deleteBrand, deleteCategory } from '../api/toyApi'
import './ToyForm.css'

function ToyForm({ toy, onSave, onCancel }) {
  const [name, setName] = useState(toy?.name || '')
  const [price, setPrice] = useState(toy?.price || '')
  const [quantity, setQuantity] = useState(toy?.quantity || '')
  const [brandId, setBrandId] = useState('')
  const [imageFile, setImageFile] = useState(null)
  const [imagePreview, setImagePreview] = useState('')
  const [selectedCategories, setSelectedCategories] = useState([])

  const [brands, setBrands] = useState([])
  const [categories, setCategories] = useState([])
  const [newBrand, setNewBrand] = useState('')
  const [newCategory, setNewCategory] = useState('')
  const [showAddBrand, setShowAddBrand] = useState(false)
  const [showAddCat, setShowAddCat] = useState(false)

  useEffect(() => { loadData() }, [])

  const loadData = async () => {
    try {
      const [bRes, cRes] = await Promise.all([getBrands(), getCategories()])
      setBrands(bRes.data)
      setCategories(cRes.data)
      if (toy) {
        const b = bRes.data.find(br => br.name === toy.brand)
        if (b) setBrandId(b.id)
        setSelectedCategories(toy.categories || [])
      }
    } catch (err) {
      console.error('Ошибка загрузки данных формы:', err)
    }
  }

  const handleFileChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setImageFile(file)
      setImagePreview(URL.createObjectURL(file))
    }
  }

  const toggleCategory = (catName) => {
    setSelectedCategories(prev =>
      prev.includes(catName)
        ? prev.filter(c => c !== catName)
        : [...prev, catName]
    )
  }

  const handleAddBrand = async () => {
    if (!newBrand.trim()) return
    try {
      const res = await createBrand({ name: newBrand })
      setBrands(prev => [...prev, res.data])
      setBrandId(res.data.id)
      setNewBrand('')
      setShowAddBrand(false)
    } catch (err) {
      alert('Ошибка добавления бренда')
    }
  }

  const handleAddCategory = async () => {
    if (!newCategory.trim()) return
    try {
      const res = await createCategory({ name: newCategory })
      setCategories(prev => [...prev, res.data])
      setNewCategory('')
      setShowAddCat(false)
    } catch (err) {
      alert('Ошибка добавления категории')
    }
  }

  const handleDeleteBrand = async (id) => {
    if (!window.confirm('Удалить бренд?')) return
    try {
      await deleteBrand(id)
      setBrands(prev => prev.filter(b => b.id != id))
      if (brandId == id) setBrandId('')
    } catch (err) {
      alert('Ошибка удаления бренда')
    }
  }

  const handleDeleteCategory = async (id) => {
    if (!window.confirm('Удалить категорию?')) return
    try {
      await deleteCategory(id)
      setCategories(prev => prev.filter(c => c.id != id))
      setSelectedCategories(prev => {
        const deleted = categories.find(c => c.id == id)
        return deleted ? prev.filter(c => c !== deleted.name) : prev
      })
    } catch (err) {
      alert('Ошибка удаления категории')
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    const catIds = categories
      .filter(c => selectedCategories.includes(c.name))
      .map(c => c.id)
    onSave({
      name,
      price: Number(price),
      quantity: Number(quantity),
      brandId: Number(brandId),
      categoryIds: catIds,
      imageName: imageFile ? imageFile.name : null,
    })
  }

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h2>{toy ? '✏️ Редактировать' : '➕ Новая игрушка'}</h2>
        <form onSubmit={handleSubmit}>
          <input placeholder="Название" value={name} onChange={(e) => setName(e.target.value)} required />
          <input type="number" placeholder="Цена (Br)" value={price} onChange={(e) => setPrice(e.target.value)} required />
          <input type="number" placeholder="Количество" value={quantity} onChange={(e) => setQuantity(e.target.value)} required />

          <div className="form-field">
            <div className="select-row">
              <select value={brandId} onChange={(e) => setBrandId(e.target.value)} required>
                <option value="">Выбери бренд</option>
                {brands.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
              </select>
              {brandId && (
                <button type="button" className="btn-delete-row" onClick={() => handleDeleteBrand(brandId)}>🗑️</button>
              )}
            </div>
            {!showAddBrand ? (
              <button type="button" className="btn-add-small" onClick={() => setShowAddBrand(true)}>+ Бренд</button>
            ) : (
              <div className="add-inline">
                <input placeholder="Новый бренд" value={newBrand} onChange={(e) => setNewBrand(e.target.value)} />
                <button type="button" className="btn-save-small" onClick={handleAddBrand}>✓</button>
                <button type="button" className="btn-cancel-small" onClick={() => setShowAddBrand(false)}>✕</button>
              </div>
            )}
          </div>

          <div className="form-image-upload">
            <label className="file-label">📷 Выбрать картинку
              <input type="file" accept="image/*" onChange={handleFileChange} hidden />
            </label>
            {imagePreview && <img src={imagePreview} alt="preview" className="image-preview" />}
          </div>

          <div className="form-categories">
            <div className="form-cat-header">
              <p>Категории:</p>
              {!showAddCat ? (
                <button type="button" className="btn-add-small" onClick={() => setShowAddCat(true)}>+ Категория</button>
              ) : (
                <div className="add-inline">
                  <input placeholder="Новая категория" value={newCategory} onChange={(e) => setNewCategory(e.target.value)} />
                  <button type="button" className="btn-save-small" onClick={handleAddCategory}>✓</button>
                  <button type="button" className="btn-cancel-small" onClick={() => setShowAddCat(false)}>✕</button>
                </div>
              )}
            </div>
            <div className="form-cat-list">
              {categories.map(c => (
                <div key={c.id} className="cat-row">
                  <label className="form-cat-check">
                    <input type="checkbox" checked={selectedCategories.includes(c.name)} onChange={() => toggleCategory(c.name)} />
                    {c.name}
                  </label>
                  <button type="button" className="btn-cat-delete" onClick={() => handleDeleteCategory(c.id)}>🗑️</button>
                </div>
              ))}
            </div>
          </div>
          <div className="form-actions">
            <button type="submit">Сохранить</button>
            <button type="button" className="btn-cancel" onClick={onCancel}>Отмена</button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default ToyForm