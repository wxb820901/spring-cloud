pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        echo 'begin build !!!'
        echo 'build over !!!'
        sh 'mvn clean install -pl demo-eureka dockerfile:build;mvn clean install -pl demo-config dockerfile:build'
      }
    }
  }
}