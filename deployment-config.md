# Deployment Configuration

## Infrastructure Details

### Jenkins Server
- **Address**: http://ec2-54-234-94-174.compute-1.amazonaws.com:8080/
- **GitHub Hook URL**: http://ec2-54-234-94-174.compute-1.amazonaws.com:8080/github-webhook/

### Database
- **URL**: jdbc:mysql://revagenda-database.c4re0g4kwp4b.us-east-1.rds.amazonaws.com:3306
- **Database Name**: movies_db_10
- **Username**: isaacpena
- **Password**: m33LPuZkq8Cfdbg (stored securely in S3)

### AWS S3 Buckets
- **Secret Bucket**: kyles-secret-bucket
- **Static Host Bucket**: trng2309-10
- **Static Host URL**: http://trng2309-10.s3-website.us-east-2.amazonaws.com/

### Application Ports
- **Backend External Port**: 8090
- **Backend Internal Port**: 8080

## Setup Instructions

1. **Configure GitHub Webhook**:
   - Go to your GitHub repository settings
   - Add webhook URL: http://ec2-54-234-94-174.compute-1.amazonaws.com:8080/github-webhook/
   - Set content type to application/json
   - Select "Just the push event"

2. **Upload application.properties to S3**:
   - Upload the `application-production.properties` file to: s3://kyles-secret-bucket/application.properties
   - This contains the secure database credentials (username: isaacpena)
   - Ensure Jenkins has permission to read from this bucket

3. **Configure AWS Permissions**:
   - Jenkins server needs permissions to:
     - Read from kyles-secret-bucket
     - Write to trng2309-10 bucket
     - Build and run Docker containers

4. **Frontend Build Output**:
   - React app builds to `movie-project-frontend/dist`
   - This gets deployed to S3 bucket trng2309-10