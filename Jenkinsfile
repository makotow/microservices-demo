podTemplate(
  label: 'skaffold',
  cloud: 'kubernetes user20',
  containers: [
    containerTemplate(name: 'skaffold-insider', image: 'registry.ndxlab.net/library/skaffold-docker:1.0-v0.34.0', ttyEnabled: true, command: 'cat')
  ],
  volumes: [
    hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')
  ]
) {
  node('skaffold') {
    withCredentials([
      usernamePassword(credentialsId: 'docker_id', usernameVariable: 'DOCKER_ID_USR', passwordVariable: 'DOCKER_ID_PSW')
    ]) {
      stage('Info') {
        container('skaffold-insider') {
          sh """
            uname -a
            whoami
            pwd
            ls -al
          """
        }
      }
      stage('Test skaffold') {
        git 'http://192.168.1.211/git/user10/msa-demo.git'
        container('skaffold-insider') {
          sh """
            docker login https://registry.ndxlab.net --username=$DOCKER_ID_USR --password=$DOCKER_ID_PSW
            SKAFFOLD_DEFAULT_REPO=registry.ndxlab.net/user20 skaffold run
          """
        }
      }
    }
  }
}
