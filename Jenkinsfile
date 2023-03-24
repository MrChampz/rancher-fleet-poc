pipeline {
  agent any
  stages {

    stage('Checkout code') {
      steps {
        sh "git checkout -B ${TARGET_BRANCH}"
      }
    }

    stage('Build Docker image') {
      steps {
        script {
          // Build the image using Dockerfile
          def image = docker.build("felpsmac/rancher-fleet-poc", "./app")
          // Push image to the registry
          docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
            image.push("${GIT_COMMIT}")
            image.push("latest")
          }
        }
      }
    }

    stage('Download Kustomize') {
      when { not { expression { return fileExists ('.k8s/base/kustomize') }}}
      steps {
        dir('.k8s/base') {
          sh 'curl -s "https://raw.githubusercontent.com/kubernetes-sigs/kustomize/master/hack/install_kustomize.sh"  | bash'
        }
      }
    }

    stage('Update app manifest') {
      steps {
        dir('.k8s/base') {
          sh "./kustomize edit set image app=felpsmac/rancher-fleet-poc:${GIT_COMMIT}"
        }
      }
    }

    stage('Commit updated manifest') {
      steps {
        script {
          // git credentialsId: 'github', url: 'https://github.com/MrChampz/rancher-fleet-poc.git'

          sh 'git add .k8s/base/kustomization.yml'
          sh "git commit -m 'Update app version to ${GIT_COMMIT} [skip ci]'"
          sh 'git push origin main'
          // withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
          //   sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/MrChampz/rancher-fleet-poc.git"
          // }
        }
      }
    }
  }
}