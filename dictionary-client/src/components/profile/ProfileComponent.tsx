import {useDictionaryClient} from "../../client/dictionary-client";

function ProfileComponent() {
    const client = useDictionaryClient();

    return (
        <>
            <h2>Profile</h2>
            <div>user id: {client.getUserDetails().userId}</div>
        </>
    )
}

export default ProfileComponent;
