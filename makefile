build:
	cd ./ErrorTicketManager && \
	rm ./build/libs/*.jar && \
	./gradlew bootJar

run: build
	#cd ./ErrorTicketManager && docker compose up --build -d
	#cd ./Frontend && docker compose up --build -d
	docker compose up --build -d
