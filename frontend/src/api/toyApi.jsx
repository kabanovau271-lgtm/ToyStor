import axios from 'axios'

const API = axios.create({
  baseURL: 'http://localhost:8080',
})

export const getToys = (page = 0, size = 10) =>
  API.get(`/toys?page=${page}&size=${size}`)

export const getToyById = (id) =>
  API.get(`/toys/${id}`)

export const searchToys = (name) =>
  API.get(`/toys/search?name=${name}`)

export const filterToys = (params = {}) =>
  API.get('/toys/filter', {
    params: {
      category: params.category || '',
      minPrice: params.minPrice || '0',
      maxPrice: params.maxPrice || '999999',
      page: params.page || 0,
      size: params.size || 6,
    }
  })

export const createToy = (data) =>
  API.post('/toys', data)

export const updateToy = (id, data) =>
  API.put(`/toys/${id}`, data)

export const deleteToy = (id) =>
  API.delete(`/toys/${id}`)

export const getCategories = () =>
  API.get('/categories')

export const getBrands = () =>
  API.get('/brands')

export const createBrand = (data) =>
  API.post('/brands', data)

export const createCategory = (data) =>
  API.post('/categories', data)

export const createOrder = (data) =>
  API.post('/orders', data)

export const getOrders = () =>
  API.get('/orders')

export const deleteOrder = (id) =>
  API.delete(`/orders/${id}`)

  export const deleteBrand = (id) =>
    API.delete(`/brands/${id}`)

  export const deleteCategory = (id) =>
    API.delete(`/categories/${id}`)