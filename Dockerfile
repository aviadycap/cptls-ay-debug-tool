FROM 607827849963.dkr.ecr.eu-west-1.amazonaws.com/cptls/base-java-openj9:21-extractor-template as extractor

FROM 607827849963.dkr.ecr.eu-west-1.amazonaws.com/cptls/base-java-openj9:21-runner-template
COPY --from=extractor /usr/src/app/.ignore /usr/src/app/