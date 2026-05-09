pipeline {
    agent { docker { image 'eclipse-temurin:17-jdk-alpine' } }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }
        stage('Test') {
            parallel {
                stage('Transaction Service') {
                    steps { dir('transaction-service') { sh 'mvn test' } }
                }
                stage('Auth Service') {
                    steps { dir('auth-service') { sh 'mvn test' } }
                }
            }
        }
        stage('Build Images') {
            steps {
                sh 'docker build -t banking/transaction-service:${BUILD_NUMBER} transaction-service/'
                sh 'docker build -t banking/auth-service:${BUILD_NUMBER} auth-service/'
            }
        }
        stage('Push to ECR') {
            steps {
                sh 'aws ecr get-login-password | docker login --username AWS --password-stdin $ECR_REGISTRY'
                sh 'docker push $ECR_REGISTRY/transaction-service:${BUILD_NUMBER}'
            }
        }
        stage('Deploy to K8s') {
            steps {
                sh 'kubectl set image deployment/transaction-service transaction-service=$ECR_REGISTRY/transaction-service:${BUILD_NUMBER}'
                sh 'kubectl rollout status deployment/transaction-service'
            }
        }
    }
    post {
        failure { mail to: 'devops@mufg-bank.com', subject: 'Build Failed', body: "${env.JOB_NAME} #${env.BUILD_NUMBER}" }
    }
}
