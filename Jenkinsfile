pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        echo 'begin build !!!'
        echo 'build over !!!'
        sh 'java -version && mvn -v && git --version && gradle -v && docker -v'
      }
    }
  }
}