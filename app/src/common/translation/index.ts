import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import { resources } from "./resource/_index";

const defaultLanguage = navigator.language.split("-")[0];

i18n.use(initReactI18next).init({
  resources,
  lng: defaultLanguage,
  fallbackLng: "en",
  interpolation: {
    escapeValue: false,
  },
});
