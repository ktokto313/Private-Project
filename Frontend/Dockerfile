FROM node:24-alpine
WORKDIR /usr/src
RUN adduser -D frontend && adduser frontend frontend 
COPY --chown=frontend:frontend ./src/. ./src/
COPY --chown=frontend:frontend ./*.* ./
RUN npm install
RUN chown frontend:frontend ./
RUN ls -al
USER frontend:frontend
RUN npm run build
EXPOSE 4173
CMD ["npm", "run", "preview"]

