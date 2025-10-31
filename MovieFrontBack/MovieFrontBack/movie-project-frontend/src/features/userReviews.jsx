import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const UserReviews = () => {

    const [reviews, setReviews] = useState([]);
    const [editForm, setEditForm] = useState({});
    const [editingReviewId, setEditingReviewId] = useState(null);



    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) return;

        const decoded = jwtDecode(token);

        const fetchData = async() => {axios.get(`http://localhost:8081/reviews/account/${decoded.sub}`)
      .then(response => {
        setReviews(response.data);
      })}
      fetchData();
    }, []);



    const handleEditClick = (review) => {
      setEditingReviewId(review.reviewId);
      setEditForm({ accountId: review.accountId,
        movieId: review.movieId, 
        reviewText: review.reviewText, 
        rating: review.rating });
    }
    const handleSaveEdit = async (reviewId) => {
      const token = localStorage.getItem("token");
      if (!token) return;
      try {
        await axios.put(`http://localhost:8081/reviews/${reviewId}`, editForm, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
      } catch (error) {
        console.log(jwtDecode(token));
        console.log(editForm);
        console.error('Error updating review:', error);
      }
      finally {
        window.location.reload();
      }
    }


    const deleteItem = async (itemId) => {
    try {
        const response = await axios.delete(`http://localhost:8081/reviews/delete/${itemId}`); // Replace with your API endpoint
        console.log('Item deleted successfully:', response.data);
        
        // Handle successful deletion (e.g., refresh data, display success message)
    } catch (error) {
        console.error('Error deleting item:', error);
        // Handle error (e.g., display error message)
    }
    finally {
      window.location.reload();
    }
  }

  return (
    <div>
      <h1>User Reviews Page</h1>
        <p>This is where user reviews will be displayed.</p>
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
                  <div className="review-rating">
                    {editingReviewId === review.reviewId ? (
                      <div>
                        <input
                          type="text"
                          value={editForm.reviewText}
                          onChange={(e) => setEditForm({ ...editForm, reviewText: e.target.value })}
                        />
                        <div className="star-rating-edit">
                          {[...Array(5)].map((_, i) => (
                            <span
                              key={i}
                              className={`star ${i < editForm.rating ? 'filled' : ''}`}
                              style={{ cursor: 'pointer', fontSize: '20px' }}
                              onClick={() => setEditForm({ ...editForm, rating: i + 1 })}
                            >
                              ★
                            </span>
                          ))}
                        </div>
                        <button onClick={() => handleSaveEdit(review.reviewId)}>Save</button>
                      </div>
                    ) : (
                      [...Array(5)].map((_, i) => (
                        <span key={i} className={`star ${i < review.rating ? 'filled' : ''}`}>
                          ★
                        </span>
                      ))
                    )}
                  </div>
                  <p className="review-text">{review.reviewText}</p>
                  <p className="review-username">- {review.accountId.username}</p>
                  <div className="review-actions" style={{ marginTop: '10px' }}>
                    <button onClick={() => deleteItem(review.reviewId)} style={{ marginRight: '10px' }}>Delete</button>
                    <button onClick={() => handleEditClick(review)}>Edit</button>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <p>No reviews yet. Be the first to review!</p>
          )}
        </div>
    </div>
    
  );
  };

export default UserReviews;