// Work in progress.

pipeline {
    agent {
        kubernetes {
            cloud 'kubernetes'
            defaultContainer 'jnlp'
            yamlFile 'pipeline/skaffoldContainer.yaml'
        }
    }
    environment {
    }

    parameters {
        string(name: 'REGISTRY_REPO_URL', defaultValue: 'gcr.io/hm-hands-on', description: 'イメージレジストリ')
        string(name: 'REPO_NAME', defaultValue: 'user99', description: 'レジストリ名')
        string(name: 'NAMESPACE', defaultValue: 'msa-demo', description: 'デプロイ先のネームスペース')
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


        stage('Clone source code') {
            steps {
                checkout scm
            }
        }


        stage('RUN skaffold: Build image and deploy') {
            steps {
                script {
                    withCredentials([
                        usernamePassword(credentialsId: 'registry_id', usernameVariable: 'REGISTRY_ID_USER', passwordVariable: 'REGISTRY_ID_PASSWORD')
                    ]) {
                        container('skaffold-container') {
                            sh """
                                docker login https://${REGISTRY_REPO_URL} -u '${REGISTRY_ID_USER}' -p '${REGISTRY_ID_PASSWORD}'
                                skaffold run -d ${REGISTRY_REPO_URL}/${REPO_NAME} -n ${NAMESPACE}
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
//    post {
//        always {
//            echo "Post process..."
//        }
//    }
}
