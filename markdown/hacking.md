# AWS 사용중에 DB 해킹?

### AWS RDS 해킹당하다
* 어느날 갑자기 배포 사이트가 404 에러를 발생시킨다
* ![hacking1](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/67e10972-89c0-4cea-9bf0-3983a292f6a1)
* AWS RDS를 확인해보니 다음과 같이 DB가 바뀌어 있었다..
* ![해킹3](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/316106b9-dbab-48ce-8e57-05f7ebfdb5a5)
* ![해킹](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/7e947850-3215-42a8-ab22-49e4e611c19e)
* ![해킹2](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/a7bd42d3-a985-49b6-843d-d4765773715a)

* DB를 복구해줄테니 비트코인을 달라는.... 말도 안되는 소리였다
* 일단 이를 해결하기 위해 AWS 보안규칙부터 살펴보기로 했다.

### AWS 보안규칙 재설정하기
1. AWS RDS 보안규칙을 확인하기 위해 AWS RDS 콘솔에서 VPC 보안 그룹을 들어간다
2. 보안그룹 ID에 값을 클릭하고 인바운드 규칙 편집에 들어간다
3. 유형의 MySQL과 SSH의 서브넷을 확인한다.

* ![hacking2](https://github.com/TwoEther/ShoppingMall_Project/assets/101616106/a4ff48c0-dcfa-4d50-9a39-61d36ff0d024)
* MySQL과 SSH가 모든 IP에 대해 개방되었었다, 그래서 해킹범이 DB를 쉽게 접속하여 날려버린것 같다.
* 노란색 박스의 public 서브넷을 삭제해서 문제를 해결했다. 
* 다음 부터는 세세한 보안규칙에도 신경을 써야겠다

