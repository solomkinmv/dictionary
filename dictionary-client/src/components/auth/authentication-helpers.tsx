import React from "react";
import jwtDecode, {JwtPayload} from "jwt-decode";

export const getToken: () => string = () => {
    return localStorage.getItem('token')!
}


interface AuthContextType {
    user: string;
    token: string;
    signin: (token: string, callback: VoidFunction) => void;
    signout: (callback: VoidFunction) => void;

    isAuthenticated: () => boolean;
}

let AuthContext = React.createContext<AuthContextType>(null!);

export function AuthProvider({children}: { children: React.ReactNode }) {
    let [token, setToken] = React.useState<string>(localStorage.getItem("token") || "");
    let [user, setUser] = React.useState<string>(token ? extractUserIdClaimFromToken(token) : "");

    let signin = (token: string, callback: VoidFunction) => {
        const userId = extractUserIdClaimFromToken(token);
        localStorage.setItem("token", token);
        setUser(userId);
        setToken(token);
        callback();
    };

    let signout = (callback: VoidFunction) => {
        localStorage.removeItem("token");
        setUser("");
        setToken("");
        callback();
    };

    let isAuthenticated = () => {
        return !!user;
    }

    let value = {user, token, signin, signout, isAuthenticated};

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

function extractUserIdClaimFromToken(token: string): string {
    const decodedToken = jwtDecode<JwtPayload>(token)
    const userId = decodedToken.sub
    if (!userId) {
        throw new Error('No user id found in token')
    }
    return userId
}

export default function useAuth() {
    return React.useContext(AuthContext);
}

