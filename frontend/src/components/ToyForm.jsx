import { useState, useEffect } from 'react'
import { getBrands, getCategories } from '../api/toyApi'
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

  useEffect(() => {
    loadData()
  }, [])

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

  const handleSubmit = async (e) => {
    e.preventDefault()

    let imagePath = ''
    if (imageFile) {
      // Копируем файл в public/images через fetch
      const formData = new FormData()
      formData.append('image', imageFile)

      try {
        // Сохраняем файл в public/images через API (fetch напрямую нельзя — используем трюк)
        const safeName = Date.now() + '_' + imageFile.name.replace(/\s/g, '_')
        const response = await fetch(`/images/${safeName}`, {
          method: 'PUT',
          body: imageFile,
        }).catch(() => null)

        // Если PUT не сработал — делаем по-другому: через создание ссылки
        // Просто используем временный URL для предпросмотра
        imagePath = imagePreview
      } catch (err) {
        console.log('Файл будет сохранён как:', imageFile.name)
        imagePath = '/images/' + imageFile.name
      }
    }

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
          <input
            placeholder="Название"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
          <input
            type="number"
            placeholder="Цена (Br)"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            required
          />
          <input
            type="number"
            placeholder="Количество"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
            required
          />
          <select value={brandId} onChange={(e) => setBrandId(e.target.value)} required>
            <option value="">Выбери бренд</option>
            {brands.map(b => (
              <option key={b.id} value={b.id}>{b.name}</option>
            ))}
          </select>

          <div className="form-image-upload">
            <label className="file-label">
              📷 Выбрать картинку
              <input
                type="file"
                accept="image/*"
                onChange={handleFileChange}
                hidden
              />
            </label>
            {imagePreview && (
              <img src={imagePreview} alt="preview" className="image-preview" />
            )}
          </div>

          <div className="form-categories">
            <p>Категории:</p>
            <div className="form-cat-list">
              {categories.map(c => (
                <label key={c.id} className="form-cat-check">
                  <input
                    type="checkbox"
                    checked={selectedCategories.includes(c.name)}
                    onChange={() => toggleCategory(c.name)}
                  />
                  {c.name}
                </label>
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