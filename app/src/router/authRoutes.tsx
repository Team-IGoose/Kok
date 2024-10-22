import { RouteObject } from "react-router-dom";
import Test from "../common/component/test";
import Splash from "@domain/splash/_splash.page";

const authRoutes: RouteObject[] = [
  {
    path: "/",
    element: <Test />,
  },
  {
    path: "/splash",
    element: <Splash />,
  },
];
export default authRoutes;
