import React, { useState, useEffect } from 'react';
import '../App.css';
import axios from 'axios'
import Coraline from '/posterImages/Coraline.jpg';
import { useParams, Link } from "react-router-dom";

const AllReviewsBy  = () => {

    const [reviews, setReviews] = useState([]);
    const { username } = useParams();

    useEffect(() => {
    axios.get(`http://localhost:8081/reviews/account/${username}`)
      .then(response => {
        setReviews(response.data);
      })
    }, []);

    return (
      
      <div>
      <h1>Reviews</h1>
        <p>These are all the reviews made by {username}</p>
        <div className="reviews-list">
          {reviews.length > 0 ? (
            reviews.map((review) => (
              
              <div key={review.reviewId} className="review-item" style={{ display: 'flex', border: '1px solid #ddd', borderRadius: '8px', padding: '15px', margin: '10px 0', gap: '15px' }}>
                
                <div className="movie-poster" style={{ flexShrink: 0 }}>
                  <img 
                    src={`/posterImages/${review.movieId.name}.jpg`} 
                    alt={`${review.movieId.title} poster`}
                    style={{ 
                      width: '120px', 
                      height: '180px', 
                      objectFit: 'cover', 
                      borderRadius: '8px',
                      border: '2px solid #333'    
                    }}
                    onError={(e) => {
                      e.target.src = `/posterImages/${review.movieId.name}`; // Fallback image
                    }}
                  />
                  <p style={{ textAlign: 'center', marginTop: '8px', fontWeight: 'bold', fontSize: '14px' }}>
                    {review.movieId.title}
                  </p>
                </div>
                <div className="review-content" style={{ flex: 1 }}>
                  <div className="review-rating" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '10px', width: '100%', marginTop: '8px' }}>
                    {/* Render up to 5 stars based on review.rating (rounded). */}
                    <div aria-hidden="true" style={{ fontSize: '20px', lineHeight: 1 }}>
                      {Array.from({ length: 5 }).map((_, i) => {
                        const ratingValue = Number(review.rating) || 0;
                        const filled = i < Math.round(ratingValue);
                        return (
                          <span
                            key={i}
                            style={{
                              color: filled ? '#FFD700' : '#ddd',
                              marginRight: '2px',
                            }}
                          >
                            ★
                          </span>
                        );
                      })}
                    </div>
                  </div>
                  <p className="review-text" style={{ marginTop: '60px' }}>{review.reviewText}</p>
                </div>
              </div>
              
            ))
          ) : (
            <p>No reviews yet. Be the first to review!</p>
          )}
        </div>
    </div>
    )

}

export default AllReviewsBy;