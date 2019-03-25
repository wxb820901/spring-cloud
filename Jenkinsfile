pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        echo 'begin build !!!'
        sh 'pwd && mvn clean install -pl demo-eureka dockerfile:build && mvn clean install -pl demo-config dockerfile:build'
        echo 'build over !!!'
      }
    }
  }
}