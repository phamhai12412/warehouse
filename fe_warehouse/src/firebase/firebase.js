// Import the functions you need from the SDKs you need

import { getAnalytics } from "firebase/analytics";
import { getStorage } from "firebase/storage";
import { initializeApp } from "firebase/app";

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyBsrbDx3B1PEoNBhy4MgqKsegBRxottXQU",
  authDomain: "projectfinal-eb454.firebaseapp.com",
  projectId: "projectfinal-eb454",
  storageBucket: "projectfinal-eb454.appspot.com",
  messagingSenderId: "500716050279",
  appId: "1:500716050279:web:308aa576f95ccc797b81c2",
  measurementId: "G-HXB663WNVG"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

export const firebaseStorage = getStorage(app);
