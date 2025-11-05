pipeline {
    agent any
    
    environment {
        S3_BUCKET = 'trng2309-10'
        SECRET_BUCKET = 's3://kyles-secret-bucket/team10/application.properties'
        AWS_REGION = 'us-east-2'
        DOCKER_IMAGE = 'spring-backend'
        EXTERNAL_PORT = '8090'
        INTERNAL_PORT = '8080'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/PenCC17/MovieProject.git'
            }
        }
        
        stage('Build React Frontend') {
            steps {
                dir('movie-project-frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }
        
        stage('Deploy Frontend to S3') {
            steps {
                dir('movie-project-frontend/dist') {
                    sh 'aws s3 sync . s3://${S3_BUCKET}/ --delete --region ${AWS_REGION}'
                }
            }
        }

        stage('Fetch Secrets') {
            steps {
                sh 'aws s3 cp s3://${SECRET_BUCKET}/application.properties moviesproject/src/main/resources/'
            }
        }
        
        stage('Build Spring Backend') {
            steps {
                dir('moviesproject') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Cleanup Old Docker Resources') {
            steps {
                sh '''
                    docker stop ${DOCKER_IMAGE} || true
                    docker rm ${DOCKER_IMAGE} || true
                    docker system prune -a -f
                '''
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .'
                sh 'docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest'
            }
        }
        
        stage('Deploy Backend Container') {
            steps {
                sh 'docker run -d --name ${DOCKER_IMAGE} -p ${EXTERNAL_PORT}:${INTERNAL_PORT} ${DOCKER_IMAGE}:latest'
            }
        }
    }
    
    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}