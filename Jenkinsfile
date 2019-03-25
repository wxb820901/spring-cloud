pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'java -version && mvn -v && git --version && gradle -v && docker -v && pwd'
      }
    }
  }
}