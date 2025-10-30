import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const UserReviews = () => {

    const [reviews, setReviews] = useState([]);
    //const [editReview, setEditReview] = useState(null);
    //const [editForm, setEditForm] = useState({reviewText : '', rating : 0})
    //const [decoded, setDecoded] = useState(null);


    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) return;

        const decoded = jwtDecode(token);
        const accountId = decoded.sub; // or whatever your JWT uses
        console.log("User ID:", decoded);

        const fetchData = async() => {axios.get(`http://localhost:8081/reviews/account/${decoded.sub}`)
      .then(response => {
        setReviews(response.data);
      })}
      fetchData();
    }, []);




    const deleteItem = async (itemId) => {
    try {
        const response = await axios.delete(`http://localhost:8081/reviews/delete/${itemId}`); // Replace with your API endpoint
        console.log('Item deleted successfully:', response.data);
        
        // Handle successful deletion (e.g., refresh data, display success message)
    } catch (error) {
        console.error('Error deleting item:', error);
        // Handle error (e.g., display error message)
    }
    }

    

    
  return (
    <div>
      <h1>User Reviews Page</h1>
        <p>This is where user reviews will be displayed.</p>
        <div className="reviews-list">
          {reviews.length > 0 ? (
            reviews.map((review) => (
              <div key={review.reviewId} className="review-item">
                <div className="review-rating">
                  {[...Array(5)].map((_, i) => (
                    <span key={i} className={`star ${i < review.rating ? 'filled' : ''}`}>
                      ★
                    </span>
                  ))}
                </div>
                <p className="review-text">{review.reviewText}</p>
                <p className="review-username">- {review.accountId.username}</p>
                <button onClick={() => deleteItem(review.reviewId)}  >Delete</button>
                <button onClick={window.open()}  >Update</button>
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