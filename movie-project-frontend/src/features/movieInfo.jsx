import React, { useState, useEffect } from 'react';
import '../App.css';
import axios from 'axios'
import Coraline from '/posterImages/Coraline.jpg';
import { useParams, Link } from "react-router-dom";

///reviews/movie/{movieId}


const MovieInfo = () => {
  // Sample movie data (replace with actual data fetching)

  const { name, movieId } = useParams();
  const [movie, setMovie] = useState([]);
  const [reviews, setReviews] = useState([]);
  
  

  useEffect(() => {
    axios.get(`http://localhost:8080/movie/${name}`)
      .then(response => {
        setMovie(response.data);
      })
  }, []);

  useEffect(() => {
    const fetchData = async() => {axios.get(`http://localhost:8080/reviews/movie/${movieId}`)
      .then(response => {
        setReviews(response.data);
      })}
      fetchData();
  }, []);




  /* useEffect(() => {
    const fetchData = async () => {
    const reviewsResponse = await axios.get(`http://localhost:8081/reviews/movie/${movieId}`)
    setReviews(reviewsResponse.data);
      
    const accountId = reviewsResponse.data.accountId;  
    const userResponse = await axios.get(`http://localhost:8081/account/1`)
    setUsername(userResponse.data);
    
    }
    fetchData();
  }, []); */

  /* const [movie, setMovie] = useState({
    names: "Coraline",
    image: Coraline,
    director: "John Doe",
    year: 2025,
    reviews: []
  }); */

  const [reviewText, setReviewText] = useState('');
  const [rating, setRating] = useState(0);
  const [error, setError] = useState('')

  const token = localStorage.getItem("token");


  const handleReviewSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!token) {
      alert('You must be logged in to submit a review.');
      return;
    }

    try {
      const response = await axios.post(
        `http://localhost:8080/reviews/${name}`,
        {
          reviewText,
          rating: parseInt(rating)
        },
        {
          headers: {
            "Content-Type": "application/json",
            //Authorization: token
            Authorization: `Bearer ${token}`
          }
        }
      );

      if (response.status === 200) {
        // Clear form after successful submission
        setReviewText('');
        setRating(0);
        
        // Update local reviews list
        setMovie(prev => ({
          ...prev,
          reviews: [...prev.reviews, { text: reviewText, rating }]
        }));
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit review')
    }
    finally {
      window.location.reload();
    }
  }

  //console.log(reviews.sdfgsdfg);

  return (
    <div className="movie-info-container">
      {/* Movie Details Section */}
      <div className="movie-details">
        <div className="movie-poster">
          <img src={`/posterImages/${movie.name}.jpg`} alt={movie.name} />
        </div>
        <div className="movie-info">
          <h1>{movie.name}</h1>
          <h2>Director:</h2>
          <p><strong>{movie.director}</strong></p>
          <h2>Year:</h2>
          <p><strong>{movie.releaseYear}</strong></p>
        </div>
      </div>

      {/* Reviews Section */}
      <div className="reviews-section">
        <h2>Create a Review </h2>
        
        {/* Add Review Form */}
        <form onSubmit={handleReviewSubmit} className="review-form">
          <div className="rating-input">
            <label>Rating:</label>
            <div className="star-rating">
              {[1, 2, 3, 4, 5].map((star) => (
                <span
                  key={star}
                  onClick={() => setRating(star)}
                  className={`star ${star <= rating ? 'filled' : ''}`}
                >
                  ★
                </span>
              ))}
            </div>
          </div>
          <textarea
            value={reviewText}
            onChange={(e) => setReviewText(e.target.value)}
            placeholder="Write your review here..."
            required
          />
          <button type="submit">Submit</button>
        </form>

        {/* Reviews List */}
        <div className="reviews-list">
          <h2>Audience Reviews</h2>
          {reviews.length > 0 ? (
            reviews.map((review, index) => (
              <div key={index} className="review-item">
                <div className="review-rating">
                  {[...Array(5)].map((_, i) => (
                    <span key={i} className={`star ${i < review.rating ? 'filled' : ''}`}>
                      ★
                    </span>
                  ))}
                </div>
                <p className="review-text">{review.reviewText}</p>
                <p className="review-username">by <Link to={`/reviewsBy/${review.accountId.username}`} style = {{color: `#1824ca`}} className="nav-link">{review.accountId.username}</Link> </p>
              </div>
            ))
          ) : (
            <p>No reviews yet. Be the first to review!</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default MovieInfo;

