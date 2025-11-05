import React, { createContext, useState } from 'react'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'

export default function Login() {
	const [username, setUsername] = useState('')
	const [password, setPassword] = useState('')
	const [error, setError] = useState('')
	const navigate = useNavigate()


	const handleSubmit = async (e) => {
		e.preventDefault()
		setError('')

		try {
			const response = await axios.post('http://localhost:8080/login', {
				username,
				password
			})
			
			// Save auth token if your backend returns one
			if (response.data.token) {
				localStorage.setItem('token', response.data.token)
			}
			
			// Navigate to home page after successful login
			navigate('/')
		} catch (err) {
			setError(err.response?.data?.message || 'Login failed. Please check your credentials.')
		}
		window.location.href = "/";
	}

	return (
		<section className="feature-page">
			<h2>Login</h2>
			<form onSubmit={handleSubmit} className="auth-form">
				{error && <div className="error-message">{error}</div>}
				
				<div className="form-group">
					<label htmlFor="username">Username</label>
					<input
						id="username"
						type="text"
						value={username}
						onChange={(e) => setUsername(e.target.value)}
						required
						placeholder="Enter username"
					/>
				</div>

				<div className="form-group">
					<label htmlFor="password">Password</label>
					<input
						id="password"
						type="password"
						value={password}
						onChange={(e) => setPassword(e.target.value)}
						required
						placeholder="Enter password"
					/>
				</div>

				<button type="submit" className="submit-button">
					Login
				</button>
			</form>
		</section>
	)
}

