import { useTranslation } from "react-i18next";

function Test(): JSX.Element {
  const { t, i18n } = useTranslation();

  const changeLanguage = (language: string) => {
    i18n.changeLanguage(language);
  };

  return (
    <>
      <>test: {t("test")}</>
      <button onClick={() => changeLanguage("en")}>en</button>
      <button onClick={() => changeLanguage("ko")}>ko</button>
    </>
  );
}
export default Test;
