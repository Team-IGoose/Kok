import { RouteObject } from "react-router-dom";
import SignInPage from "../_domain/pages/sign.in.page";

const authRoutes: RouteObject[] = [
  {
    path: "/",
    element: <SignInPage />,
  },
];
export default authRoutes;
