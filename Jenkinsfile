pipeline {
  agent any
  stages {

    stage('Build Docker image') {
      steps {
        script {
          docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
            // Build the image using Dockerfile
            image = docker.build("felpsmac/rancher-fleet-poc", "./app")
            // Push image to the registry
            image.push("${GIT_COMMIT}")
            image.push("latest")
          }
        }
      }
    }

    stage('Download Kustomize') {
      steps {
        sh 'curl -s "https://raw.githubusercontent.com/kubernetes-sigs/kustomize/master/hack/install_kustomize.sh"  | bash'
      }
    }

    stage('Update app manifest') {
      agent {
        docker reuseNode: true, image: "place1/kube-tools:2021.06.18", args: "--entrypoint=''"
      }
      steps {
        sh "cd .k8s/base"
        sh "kustomize edit set image app=felpsmac/rancher-fleet-poc:${GIT_COMMIT}"
      }
    }

    stage('Commit updated manifest') {
      steps {
        script {
          git credentialsId: 'github', url: 'https://github.com/MrChampz/rancher-fleet-poc.git'
          sh 'git add .k8s'
          sh "git commit -m 'Update app version to ${GIT_COMMIT} [skip ci]'"
          sh 'git push origin main'
        }
      }
    }
  }
}