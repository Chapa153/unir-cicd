pipeline {
    agent {
        label 'docker'
    }
    stages {
        stage('Source') {
            steps {
                git 'https://github.com/Chapa153/unir-cicd.git'
            }
        }
        stage('Build') {
            steps {
                echo 'Building stage!'
                sh 'make build'
            }
        }
        stage('Unit tests') {
            steps {
                echo 'Running Unit Tests!'
                sh 'make test-unit'
                archiveArtifacts artifacts: 'results/unit_result.xml'
            }
        }
        stage('API tests') {
            steps {
                echo 'Running API Tests!'
                sh 'make test-api'
                archiveArtifacts artifacts: 'results/api_result.xml'
                junit 'results/api_result.xml'
            }
        }
        stage('E2E tests') {
            steps {
                echo 'Running E2E Tests!'
                sh 'make test-e2e'
                archiveArtifacts artifacts: 'results/e2e_result.xml'
                junit 'results/e2e_result.xml'
            }
        }
    }
    post {
        /*failure {
            mail to: 'team@example.com',
                subject: "Pipeline Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "The pipeline ${env.JOB_NAME} #${env.BUILD_NUMBER} has failed. Please check Jenkins for more details."
        }*/
        always {
            junit 'results/*_result.xml'
            cleanWs()
        }
    }
}
