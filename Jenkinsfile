podTemplate(
  label: 'skaffold',
  containers: [
    containerTemplate(name: 'skaffold-insider', image: 'dockerce/skaffold-example:v0.27.0', ttyEnabled: true, command: 'cat')
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
            skaffold run
          """
        }
      }
    }
  }
}
