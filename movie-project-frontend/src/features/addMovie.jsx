import React, { useState } from 'react';
import axios from 'axios';

export default function AddMovie() {
    const [movie, setMovie] = useState({
        name: '',
        director: '',
        releaseYear: '',
        posterImage: null
    });

    const [dragActive, setDragActive] = useState(false);
    const [imagePreview, setImagePreview] = useState(null);

    // Handle file upload
    const handleFileChange = (file) => {
        if (file && file.type.startsWith('image/')) {
            setMovie({ ...movie, posterImage: file });
            
            // Create preview
            const reader = new FileReader();
            reader.onload = (e) => setImagePreview(e.target.result);
            reader.readAsDataURL(file);
        } else {
            alert('Please select a valid image file');
        }
    };

    // Handle drag events
    const handleDrag = (e) => {
        e.preventDefault();
        e.stopPropagation();
        if (e.type === "dragenter" || e.type === "dragover") {
            setDragActive(true);
        } else if (e.type === "dragleave") {
            setDragActive(false);
        }
    };

    // Handle drop event
    const handleDrop = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setDragActive(false);
        
        if (e.dataTransfer.files && e.dataTransfer.files[0]) {
            handleFileChange(e.dataTransfer.files[0]);
        }
    };

    // Handle file input change
    const handleInputChange = (e) => {
        if (e.target.files && e.target.files[0]) {
            handleFileChange(e.target.files[0]);
        }
    };

    const handleMovieSubmit = async (e) => {
        e.preventDefault();
        try {
            // Create FormData for file upload
            const formData = new FormData();
            formData.append('name', movie.name);
            formData.append('director', movie.director);
            formData.append('releaseYear', movie.releaseYear);
            
            if (movie.posterImage) {
                // The backend will rename this to the movie name
                formData.append('posterImage', movie.posterImage);
            }

            const response = await axios.post('http://localhost:8081/movie/add', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            
            console.log('Movie added successfully:', response.data);
            
            // Clear the form after successful submission
            setMovie({
                name: '',
                director: '',
                releaseYear: '',
                posterImage: null
            });
            setImagePreview(null);
            
            alert('Movie added successfully with poster image!');
        } catch (error) {
            console.error('Error adding movie:', error);
            alert('Error adding movie. Please try again.');
        }
    };

    return (
        <div>
            <h2>Add Movie</h2>
            <form onSubmit={handleMovieSubmit}>
                <div>
                    <label htmlFor="name">Movie Name:</label>
                    <input
                        type="text"
                        id="name"
                        value={movie.name}
                        onChange={(e) => setMovie({ ...movie, name: e.target.value })}
                        required
                    />
                </div>  
                <div>
                    <label htmlFor="director">Director:</label>
                    <input
                        type="text"
                        id="director"
                        value={movie.director}
                        onChange={(e) => setMovie({ ...movie, director: e.target.value })}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="releaseYear">Release Year:</label>
                    <input
                        type="number"
                        id="releaseYear"
                        value={movie.releaseYear}
                        onChange={(e) => setMovie({ ...movie, releaseYear: e.target.value })}
                        min="1900"
                        max={new Date().getFullYear()}
                        required
                    />
                </div>
                
                {/* Image Upload Dropbox */}
                <div>
                    <label>Movie Poster:</label>
                    <div
                        className={`dropbox ${dragActive ? 'drag-active' : ''}`}
                        onDragEnter={handleDrag}
                        onDragLeave={handleDrag}
                        onDragOver={handleDrag}
                        onDrop={handleDrop}
                        style={{
                            border: `2px dashed ${dragActive ? '#007bff' : '#ccc'}`,
                            borderRadius: '8px',
                            padding: '40px',
                            textAlign: 'center',
                            backgroundColor: dragActive ? '#f8f9fa' : '#fafafa',
                            cursor: 'pointer',
                            transition: 'all 0.3s ease',
                            margin: '10px 0'
                        }}
                        onClick={() => document.getElementById('poster-input').click()}
                    >
                        <input
                            id="poster-input"
                            type="file"
                            accept="image/*"
                            onChange={handleInputChange}
                            style={{ display: 'none' }}
                        />
                        
                        {imagePreview ? (
                            <div>
                                <img 
                                    src={imagePreview} 
                                    alt="Preview" 
                                    style={{ 
                                        maxWidth: '200px', 
                                        maxHeight: '300px', 
                                        objectFit: 'cover',
                                        borderRadius: '4px'
                                    }} 
                                />
                                <p style={{ marginTop: '10px', color: '#666' }}>
                                    Click to change image or drag a new one here
                                </p>
                            </div>
                        ) : (
                            <div>
                                <p style={{ fontSize: '18px', margin: '10px 0' }}>📁</p>
                                <p style={{ color: '#666' }}>
                                    Drag and drop movie poster here, or click to select
                                </p>
                                <p style={{ fontSize: '14px', color: '#999' }}>
                                    Supports: JPG, PNG, GIF
                                </p>
                            </div>
                        )}
                    </div>
                </div>
                
                <button type="submit">Add Movie</button>
            </form>
        </div>
    );
}