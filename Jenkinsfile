pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        powershell(script: 'mvn clean install -pl demo-eureka dockerfile:build;mvn clean install -pl demo-config dockerfile:build', returnStatus: true, returnStdout: true, label: 'build')
      }
    }
  }
}