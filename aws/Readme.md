AWS learning account: wxb820901awslearn@126.com

aws problems:
------------------------------------------------------------------------------------------------------------------------
how to access EC2?
==> click the "connect" button (if putty used, click "connect using PuTTY)") and follow detail step.
------------------------------------------------------------------------------------------------------------------------
if change client pc how to download private key?
==>create new key pair and auto down load *.pem.
------------------------------------------------------------------------------------------------------------------------
error "Unable to locate credentials" when aws s3 cp a.txt s3://demo-bucket-20190625-1/
==> aws configure list

      Name                    Value             Type    Location
      ----                    -----             ----    --------
   profile                <not set>             None    None
access_key     ****************      shared-credentials-file
secret_key     ****************      shared-credentials-file
    region                <not set>             None    None

check if the credentials" exist.

if not

cd ~
mkdir .aws
vi .aws/credentials"

and input like below

[default]
aws_access_key_id =
aws_secret_access_key =

------------------------------------------------------------------------------------------------------------------------
2, RDS unreachable
==> enable public access
------------------------------------------------------------------------------------------------------------------------
3, before create vpc, subnet and security group. understad CIDR firstly.
------------------------------------------------------------------------------------------------------------------------
4, use pgadmin connection AWS RDS, default value of "Maintenance database" and "Username" are all postgres
------------------------------------------------------------------------------------------------------------------------
create apigateway+lambda refer to https://gist.github.com/crypticmind/c75db15fd774fe8f53282c3ccbe3d7ad
all in setup.sh

start docker as:
TMPDIR=/private$TMPDIR docker-compose -f /Users/xiaobingwang/dev/git-local/spring-cloud/aws/aws-test-case/src/test/resources/dc-macos.yml up

------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------