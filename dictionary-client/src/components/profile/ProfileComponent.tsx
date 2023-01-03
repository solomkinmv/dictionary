import {dictionaryClient} from "../../client/dictionary-client";

function ProfileComponent() {
    const client = dictionaryClient();

    return (
        <>
            <h2>Profile</h2>
            <div>user id: {client.getUserDetails().userId}</div>
        </>
    )
}

export default ProfileComponent;
