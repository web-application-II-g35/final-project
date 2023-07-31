import { createContext, useState, useEffect } from "react";
import jwtDecode from "jwt-decode";
import ProfilesAPI from "../api/profiles/profilesApi";

export const AuthContext = createContext();

export const useAuth = () => {
  const [currentUser, setCurrentUser] = useState({});

  const isTokenExpired = (token) => {
    const decodedToken = jwtDecode(token);
    if (decodedToken.exp && decodedToken.exp * 1000 < Date.now()) {
      return true;
    }
    return false;
  };

  const logout = () => {
    localStorage.removeItem("token");
    setCurrentUser({});
  };

  useEffect(() => {
    const fetchToken = async () => {
      const token = localStorage.getItem("token");

      if (token) {
        if (!isTokenExpired(token)) {
          const decodedToken = jwtDecode(token);
          const role =
            decodedToken.resource_access["springboot-keycloak-client"].roles[0];
          let id = null;
          if (role !== "Customer") {
            if (role === "Manager") {
              id = await ProfilesAPI.getManagerId(decodedToken.email);
            }
            if (role === "Expert") {
              id = await ProfilesAPI.getExpertId(decodedToken.email);
            }
          }

          setCurrentUser({
            email: decodedToken.email,
            name: decodedToken.given_name,
            surname: decodedToken.family_name,
            username: decodedToken.preferred_username,
            role: decodedToken.resource_access["springboot-keycloak-client"]
              .roles[0],
            id: id.id,
          });
        } else {
          logout();
        }
      }
    };

    fetchToken();
  }, []);

  return [currentUser, logout];
};