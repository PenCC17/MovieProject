import React from 'react'
import './App.css'
import { BrowserRouter, Routes, Route, Link, useNavigate } from 'react-router-dom'
import Login from './features/login'
import Register from './features/register'
import MovieInfo from './features/movieInfo'
import UserReviews from './features/userReviews'
import AllReviewsBy from './features/allReviewsBy'
import reactLogo from './assets/react.svg'
import axios from 'axios';

import { useEffect, useState } from 'react';


function Home() {
  const navigate = useNavigate()

  const [user, setUser] = useState(null);
  const [movies, setMovies] = useState([]);
  const movieURL = 'http://localhost:8080/movie'
  const imageNames = ['Coraline.jpg', 
                      'Inception.jpg', 
                      'Interstellar.jpg', 
                      'Parasite.jpg', 
                      'Pokemon the First movie.jpg', 
                      'Rango.jpg', 
                      'The Godfather.jpg',
                      'The Shawshank Redemption.jpg',
                      'Space Jam.jpg',
                      'Spirited Away.jpg'];


  useEffect(() => {
    axios.get(movieURL)
      .then(response => {
        setMovies(response.data);
      })
  }, []);

  /* let setMovies = { name : it.name, movieId : it.movieId, director : it.director, releaseYear : it.releaseYear, img_src : it.name.append(".jpg")} */

/* let pokemonDetails = 
            <strong>Name:</strong> ${capitalizeFirst(pokeJson.name)}<br>
            <strong>ID:</strong> #${pokeJson.id}<br>
            <strong>Height:</strong> ${pokeJson.height / 10} m<br>
            <strong>Weight:</strong> ${pokeJson.weight / 10} kg<br>
            <strong>Type(s):</strong> ${pokeJson.types.map(type => capitalizeFirst(type.type.name)).join(', ')}<br>
            <strong>Base Experience:</strong> ${pokeJson.base_experience}<br>
            <strong>Abilities:</strong> ${pokeJson.abilities.map(ability => capitalizeFirst(ability.ability.name)).join(', ')}<br>
            <strong>Stats:</strong><br>
            ${pokeJson.stats.map(stat => &nbsp;&nbsp;${capitalizeFirst(stat.stat.name)}: ${stat.base_stat}).join('<br>')}
        ; */


  function Gallery() {

  return (
    <div>
      {imageNames.map((name) => (
        <img key={name} src={`/posterImages/${name}`} alt={name} />
      ))}
    </div>
  );
}

  function handleActivate(item) {
    // Placeholder action: navigate to a route or open a page.
    // For now we'll navigate to / (replace with real route later) and show an alert.
    // Example: navigate(`/category/${item.id}`)
    navigate(`/movieInfo/${item.name}/${item.movieId}`);
    //alert(`Clicked: ${item.name}`)
  }

  function onKey(e, item) {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault()
      handleActivate(item)
    }
  }

  /* function handleLogout() {
    localStorage.removeItem("token");
    setUser(null);
  } */

  return (
    <section className="home">
      <h1>Welcome to the Movie App</h1>
      <p>Select a Movie</p>

      <div className="figures-grid">
        {movies.map((it) => (
          <figure
            key={it.movieId}
            className="figure-card"
            role="button"
            tabIndex={0}
            onClick={() => handleActivate(it)}
            onKeyDown={(e) => onKey(e, it)}
            aria-pressed="false"
          >
            {/* <img src={Coraline} alt={it.name} /> */}
            <img 
              src={`/posterImages/${it.name}.jpg`} 
              alt={it.name}
              style={{ 
                      width: '220px', 
                      height: '280px', 
                      objectFit: 'cover', 
                      borderRadius: '8px',
                      border: '2px solid #333'    
                    }} />
            <figcaption>{it.name}</figcaption>
          </figure>
        ))}
      </div>
    </section>
  )
}

function App() {
  const [dropdownOpen, setDropdownOpen] = useState(false);

  function handleLogout() {
    localStorage.removeItem("token");
    window.location.href = "/";
  }

  function toggleDropdown() {
    setDropdownOpen(!dropdownOpen);
  }

  // Close dropdown when clicking outside
  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownOpen && !event.target.closest('.dropdown')) {
        setDropdownOpen(false);
      }
    }

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [dropdownOpen]);

  return (
    <BrowserRouter>
      <header className="taskbar">
        <div className="taskbar-inner">
          <div className="nav-left">
            <Link to="/" className="nav-link brand">Home</Link>
          </div>
          <div className="nav-right">
            {localStorage.getItem("token") ? (
              <div className="dropdown">
                <button className="dropbtn" onClick={toggleDropdown}>
                  <span className="user-icon">👤</span>
                </button>
                <div className={`dropdown-content ${dropdownOpen ? 'show' : ''}`}>
                  <span>Welcome!</span>
                  <Link to="/userReviews" onClick={() => setDropdownOpen(false)}>Reviews</Link>
                  <button onClick={() => {handleLogout(); setDropdownOpen(false);}}>Logout</button>
                </div>
              </div>
            ) : (
              <>
                <Link to="/login" className="nav-link">Login</Link>
                <Link to="/register" className="nav-link">Register</Link>
              </>
            )}
          </div>
        </div>
      </header>

      <main className="app-main">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/movieInfo/:name/:movieId" element={<MovieInfo />} />
          <Route path="/userReviews" element={<UserReviews />} />
          <Route path="/reviewsBy/:username" element={<AllReviewsBy/>} />
        </Routes>
      </main>
    </BrowserRouter>
  )
}

export default App
