import React, { useState } from 'react';
import '../App.css';
import axios from 'axios'
import Coraline from '/posterImages/Coraline.jpg';

const MovieInfo = () => {
  // Sample movie data (replace with actual data fetching)
  const [movie, setMovie] = useState({
    name: "Sample Movie",
    image: Coraline,
    director: "John Doe",
    year: 2025,
    reviews: []
  });

  const [reviewText, setReviewText] = useState('');
  const [rating, setRating] = useState(0);
  const [error, setError] = useState('')

  const token = localStorage.getItem("token");


  const handleReviewSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!token) {
      setError('Please log in to submit a review');
      return;
    }

    try {
      const response = await axios.post(
        'http://localhost:8081/reviews/Coraline',
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
  }

  return (
    <div className="movie-info-container">
      {/* Movie Details Section */}
      <div className="movie-details">
        <div className="movie-poster">
          <img src={movie.image} alt={movie.name} />
        </div>
        <div className="movie-info">
          <h1>{movie.name}</h1>
          <p><strong>Director:</strong> {movie.director}</p>
          <p><strong>Year:</strong> {movie.year}</p>
        </div>
      </div>

      {/* Reviews Section */}
      <div className="reviews-section">
        <h2>Reviews</h2>
        
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
          <button type="submit">Submit Review</button>
        </form>

        {/* Reviews List */}
        <div className="reviews-list">
          {movie.reviews.length > 0 ? (
            movie.reviews.map((review, index) => (
              <div key={index} className="review-item">
                <div className="review-rating">
                  {[...Array(5)].map((_, i) => (
                    <span key={i} className={`star ${i < review.rating ? 'filled' : ''}`}>
                      ★
                    </span>
                  ))}
                </div>
                <p className="review-text">{review.text}</p>
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
