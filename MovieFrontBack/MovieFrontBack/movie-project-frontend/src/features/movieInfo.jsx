import React, { useState, useEffect } from 'react';
import '../App.css';
import axios from 'axios'
import Coraline from '/posterImages/Coraline.jpg';
import { useParams } from "react-router-dom";
///reviews/movie/{movieId}


const MovieInfo = () => {
  // Sample movie data (replace with actual data fetching)

  const { name, movieId } = useParams();
  const [movie, setMovie] = useState([]);
  const [reviews, setReviews] = useState([]);
  
  

  useEffect(() => {
    axios.get(`http://localhost:8081/movie/${name}`)
      .then(response => {
        setMovie(response.data);
      })
  }, []);

  useEffect(() => {
    const fetchData = async() => {axios.get(`http://localhost:8081/reviews/movie/${movieId}`)
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
      setError('Please log in to submit a review');
      return;
    }

    try {
      const response = await axios.post(
        `http://localhost:8081/reviews/${name}`,
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
          <p><strong>Director:</strong> {movie.director}</p>
          <p><strong>Year:</strong> {movie.releaseYear}</p>
        </div>
      </div>

      {/* Reviews Section */}
      <div className="reviews-section">
        <h2>Reviews</h2>
        <h3>Hello </h3>
        
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
                <p className="review-username">- {review.accountId.username}</p>
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

