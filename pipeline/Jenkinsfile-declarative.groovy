pipeline {
    agent {
        kubernetes {
            cloud 'kubernetes'
            defaultContainer 'jnlp'
            yamlFile 'pipeline/skaffoldContainer.yaml'
        }
    }
    environment {
        SKAFFOLD_DEFAULT_REPO = 'registry.ndxlab.net/user20'
        GIT_URL = 'http://192.168.1.211/git/user10/msa-demo.git'
        DOCKER_URL = 'https://registry.ndxlab.net'
    }
    stages {
        stage('Envinronment info') {
            steps {
                container('skaffold-container') {
                    sh """
                        uname -a
                        whoami
                        pwd
                        skaffold version
                    """
                }
            }
        }


        stage('RUN: run skaffold') {
            steps {
                script {
                    withCredentials([
                            usernamePassword(credentialsId: 'docker_id',
                                    usernameVariable: 'DOCKER_ID_USR',
                                    passwordVariable: 'DOCKER_ID_PASSWORD')
                    ]) {
                        git ${GIT_URL}
                        container('skaffold-container') {
                            sh """
                                docker login ${DOCKER_URL} --username=${DOCKER_ID_USER} --password=${DOCKER_ID_PASSWORD}
                                skaffold run 
                            """
                        }
                    }
                }
            }
        }

//        stage('Deploy apps') {
//            input {
//                message "Should we continue?"
//                ok "Yes, we should."
//                submitter "alice,bob"
//                parameters {
//                    string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
//                }
//            }
//        }

    }
    post {
        always {
            echo "Post process..."
        }
    }
}
