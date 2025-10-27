import React, { useState } from 'react'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'

export default function Register() {
	const [username, setUsername] = useState('')
	const [password, setPassword] = useState('')
	const [error, setError] = useState('')
	const navigate = useNavigate()

	const handleSubmit = async (e) => {
		e.preventDefault()
		setError('')

		try {
			await axios.post('http://localhost:8081/register', {
				username,
				password
			})
			// On successful registration, navigate to login
			navigate('/login')
		} catch (err) {
			setError(err.response?.data?.message || 'Registration failed. Please try again.')
		}
	}

	return (
		<section className="feature-page">
			<h2>Register</h2>
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
					Register
				</button>
			</form>
		</section>
	)
}
