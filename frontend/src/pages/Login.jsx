import { useState } from 'react'
import axios from 'axios'
import './Login.css'

function Login({ onLogin }) {
  const [isRegister, setIsRegister] = useState(false)
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  const handleLogin = async (e) => {
    e.preventDefault()
    try {
      const res = await axios.post('http://localhost:8080/auth/login', {
        email,
        password,
      })
      onLogin(res.data)
    } catch (err) {
      setError('Неверный email или пароль')
    }
  }

  const handleRegister = async (e) => {
    e.preventDefault()
    try {
      const res = await axios.post('http://localhost:8080/customers', {
        name,
        email,
        password,
        role: 'USER',
      })
      // После регистрации сразу авторизуем
      onLogin({
        id: res.data.id,
        name: res.data.name,
        email: res.data.email,
        role: res.data.role || 'USER',
      })
    } catch (err) {
      setError('Ошибка регистрации')
    }
  }

  return (
    <div className="login-page">
      <div className="login-card">
        <h1>🐣 Капитошка</h1>
        <p className="login-subtitle">
          {isRegister ? 'Регистрация' : 'Вход'}
        </p>

        <form onSubmit={isRegister ? handleRegister : handleLogin}>
          {isRegister && (
            <input
              type="text"
              placeholder="Имя"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          )}
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Пароль"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          {error && <p className="login-error">{error}</p>}
          <button type="submit">
            {isRegister ? 'Зарегистрироваться' : 'Войти'}
          </button>
        </form>

        <p
          className="login-hint"
          style={{ display: 'block', marginTop: '16px', cursor: 'pointer', color: '#C9A87C' }}
          onClick={() => {
            setIsRegister(!isRegister)
            setError('')
          }}
        >
          {isRegister ? 'Уже есть аккаунт? Войти' : 'Нет аккаунта? Зарегистрироваться'}
        </p>
      </div>
    </div>
  )
}

export default Login